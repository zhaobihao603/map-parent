package com.imap.cloud.common.cache.lucene.io;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisDataException;

import com.google.common.collect.Lists;
import com.google.common.primitives.Longs;
import com.imap.cloud.common.cache.lucene.Operations;
import com.imap.cloud.common.cache.lucene.util.Constants;
import static com.imap.cloud.common.cache.lucene.util.CompressUtils.compressFilter;
import static com.imap.cloud.common.cache.lucene.util.CompressUtils.uncompressFilter;
import static com.imap.cloud.common.cache.lucene.util.FileBlocksUtils.getBlockName;
import static com.imap.cloud.common.cache.lucene.util.FileBlocksUtils.getBlockSize;


public class JedisStream implements InputOutputStream {
    private String IP;
    private int port;
    private int timeout = Constants.TIME_OUT;
    private String password = Constants.PASSWORD;

    private Jedis openJedis() {
    	Jedis jedis = new Jedis(IP, port, this.timeout);
    	try{
        	jedis.auth(this.password);
		}catch(JedisDataException e){
			e.printStackTrace();
		}
        return jedis;
    }

    public JedisStream(String IP, int port, int timeout,String password) {
        this(IP, port);
        this.timeout = timeout;
        this.password = password;
    }
    
    public JedisStream(String IP, int port, int timeout) {
        this(IP, port);
        this.timeout = timeout;
    }

    public JedisStream(String IP, int port) {
        this.IP = IP;
        this.port = port;
    }

    @Override
    public Boolean hexists(byte[] key, byte[] field) {
        boolean hexists;
        Jedis jedis = null;
        try {
            jedis = openJedis();
            hexists = jedis.hexists(key, field);
        } finally {
            jedis.close();
        }
        return hexists;
    }

    @Override
    public byte[] hget(byte[] key, byte[] field, Operations operations) {
        Jedis jedis = null;
        byte[] hget;
        try {
            jedis = openJedis();
            hget = jedis.hget(key, field);
            if (operations == Operations.FILE_DATA) {
                return uncompressFilter(hget);
            }
        } finally {
            jedis.close();
        }
        return hget;
    }

    @Override
    public void close() throws IOException {
        //Noop
    }

    @Override
    public Long hdel(byte[] key, byte[]... fields) {
        Jedis jedis = openJedis();
        Long hdel = jedis.hdel(key, fields);
        jedis.close();
        return hdel;
    }

    @Override
    public Long hset(byte[] key, byte[] field, byte[] value, Operations operations) {
        Jedis jedis = openJedis();
        if (operations == Operations.FILE_DATA) {
            value = compressFilter(value);
        }
        Long hset = jedis.hset(key, field, value);
        jedis.close();
        return hset;
    }

    @Override
    public Set<byte[]> hkeys(byte[] key) {
        Jedis jedis = openJedis();
        Set<byte[]> hkeys = jedis.hkeys(key);
        jedis.close();
        return hkeys;
    }

    @Override
    public void deleteFile(String fileLengthKey, String fileDataKey, String field, long blockSize) {
        Jedis jedis = openJedis();
        Pipeline pipelined = jedis.pipelined();
        //delete file length
        pipelined.hdel(fileLengthKey.getBytes(), field.getBytes());
        //delete file content
        for (int i = 0; i < blockSize; i++) {
            byte[] blockName = getBlockName(field, i);
            pipelined.hdel(fileDataKey.getBytes(), blockName);
        }
        pipelined.sync();
        jedis.close();
    }

    @Override
    public void rename(String fileLengthKey, String fileDataKey, String oldField, String newField, List<byte[]> values, long
            fileLength) {
        Jedis jedis = openJedis();
        Pipeline pipelined = jedis.pipelined();
        //add new file length
        pipelined.hset(fileLengthKey.getBytes(), newField.getBytes(), Longs.toByteArray(fileLength));
        //add new file content
        Long blockSize = getBlockSize(fileLength);
        for (int i = 0; i < blockSize; i++) {
            pipelined.hset(fileDataKey.getBytes(), getBlockName(newField, i), compressFilter(values.get(i)));
        }
        pipelined.sync();
        jedis.close();
        values.clear();
        deleteFile(fileLengthKey, fileDataKey, oldField, blockSize);
    }

    @Override
    public void saveFile(String fileLengthKey, String fileDataKey, String fileName, List<byte[]> values, long fileLength) {
        Jedis jedis = openJedis();
        Pipeline pipelined = jedis.pipelined();
        pipelined.hset(fileLengthKey.getBytes(), fileName.getBytes(), Longs.toByteArray(fileLength));
        Long blockSize = getBlockSize(fileLength);
        for (int i = 0; i < blockSize; i++) {
            pipelined.hset(fileDataKey.getBytes(), getBlockName(fileName, i), compressFilter(values.get(i)));
            if (i % Constants.SYNC_COUNT == 0) {
                pipelined.sync();
                pipelined = jedis.pipelined();
            }
        }
        pipelined.sync();
        jedis.close();
        values.clear();
    }

    @Override
    public List<byte[]> loadFileOnce(String fileDataKey, String fileName, long blockSize) {
        Jedis jedis = openJedis();
        Pipeline pipelined = jedis.pipelined();
        List<byte[]> res = new ArrayList<>();
        List<Response<byte[]>> temps = new ArrayList<>();
        int temp = 0;
        while (temp < blockSize) {
            Response<byte[]> data = pipelined.hget(fileDataKey.getBytes(), getBlockName(fileName, temp));
            temps.add(data);
            if (temp % Constants.SYNC_COUNT == 0) {
                pipelined.sync();
                for(Response<byte[]> response:temps){
                	res.add(uncompressFilter(response.get()));
                }
                //res.addAll(temps.stream().map(response -> uncompressFilter(response.get())).collect(Collectors.toList()));
                temps.clear();
                pipelined = jedis.pipelined();
            }
            temp++;
        }
        try {
            pipelined.sync();
        } catch (JedisConnectionException e) {
            //log.error("pipelined = {}, blockSize = {}!", pipelined.toString(), blockSize);
            //log.error("", e);
        } finally {
            jedis.close();
        }
        for(Response<byte[]> response:temps){
        	res.add(uncompressFilter(response.get()));
        }
        //res.addAll(temps.stream().map(response -> uncompressFilter(response.get())).collect(Collectors.toList()));
        temps.clear();
        return res;
    }

	@Override
	public String[] getAllFileNames(String directoryMedata) {
		Objects.requireNonNull(directoryMedata);
        Set<byte[]> hkeys = hkeys(directoryMedata.getBytes());
        Objects.requireNonNull(hkeys);
        ArrayList<String> names = Lists.newArrayList();
        for(byte[] key:hkeys){
        	names.add(new String(key, StandardCharsets.UTF_8));
        }
        return names.toArray(ArrayUtils.EMPTY_STRING_ARRAY);
	}

	@Override
	public void checkTransactionResult(List<Object> exec) {
		for (Object o : exec) {
            boolean equals = StringUtils.equals(Objects.toString(o), "0");
            if (equals) {
                System.err.println("Execute transaction occurs error!");
            }
        }
	}
}
