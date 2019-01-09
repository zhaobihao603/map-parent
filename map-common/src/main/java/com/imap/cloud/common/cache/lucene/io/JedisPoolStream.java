package com.imap.cloud.common.cache.lucene.io;

import static com.imap.cloud.common.cache.lucene.util.CompressUtils.compressFilter;
import static com.imap.cloud.common.cache.lucene.util.CompressUtils.uncompressFilter;
import static com.imap.cloud.common.cache.lucene.util.FileBlocksUtils.getBlockName;
import static com.imap.cloud.common.cache.lucene.util.FileBlocksUtils.getBlockSize;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;
import redis.clients.jedis.exceptions.JedisConnectionException;

import com.google.common.collect.Lists;
import com.google.common.primitives.Longs;
import com.imap.cloud.common.cache.lucene.Operations;
import com.imap.cloud.common.cache.lucene.util.Constants;


public class JedisPoolStream implements InputOutputStream {
    private JedisPool jedisPool;

    public JedisPoolStream(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    @Override
    public Boolean hexists(byte[] key, byte[] field) {
        boolean hexists;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            hexists = jedis.hexists(key, field);
        } finally {
            jedis.close();
        }
        return hexists;
    }

    @Override
    public byte[] hget(byte[] key, byte[] field, Operations operations) {
        byte[] hget;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
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
    public void close() {
        if (jedisPool != null) {
            jedisPool.close();
        }
    }

    @Override
    public Long hdel(byte[] key, byte[]... fields) {
        long hdel;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            hdel = jedis.hdel(key, fields);
        } finally {
            jedis.close();
        }
        return hdel;
    }

    @Override
    public Long hset(byte[] key, byte[] field, byte[] value, Operations operations) {
        Long hset;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if (operations == Operations.FILE_DATA) {
                value = compressFilter(value);
            }
            hset = jedis.hset(key, field, value);
        } finally {
            jedis.close();
        }
        return hset;
    }


    @Override
    public Set<byte[]> hkeys(byte[] key) {
        Set<byte[]> hkeys;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            hkeys = jedis.hkeys(key);
        } finally {
            if(jedis!=null)jedis.close();
        }
        return hkeys;
    }

    /**
     * Use transactions to delete index file
     *
     * @param fileLengthKey
     * @param fileDataKey
     * @param field
     * @param blockSize
     */
    @Override
    public void deleteFile(String fileLengthKey, String fileDataKey, String field, long blockSize) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            Pipeline pipelined = jedis.pipelined();
            //delete file length
            pipelined.hdel(fileLengthKey.getBytes(), field.getBytes());
            //delete file content
            for (int i = 0; i < blockSize; i++) {
                byte[] blockName = getBlockName(field, i);
                pipelined.hdel(fileDataKey.getBytes(), blockName);
            }
            pipelined.sync();
        } finally {
            jedis.close();
        }
    }

    @Override
    public void rename(String fileLengthKey, String fileDataKey, String oldField, String newField, List<byte[]> values, long
            fileLength) {
        long blockSize = 0;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            Pipeline pipelined = jedis.pipelined();
            //add new file length
            pipelined.hset(fileLengthKey.getBytes(), newField.getBytes(), Longs.toByteArray(fileLength));
            //add new file content
            blockSize = getBlockSize(fileLength);
            for (int i = 0; i < blockSize; i++) {
                pipelined.hset(fileDataKey.getBytes(), getBlockName(newField, i), compressFilter(values.get(i)));
            }
            values.clear();
            pipelined.sync();
        } finally {
            jedis.close();
            deleteFile(fileLengthKey, fileDataKey, oldField, blockSize);
        }
    }

    @Override
    public void saveFile(String fileLengthKey, String fileDataKey, String fileName, List<byte[]> values, long fileLength) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
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
            values.clear();
            pipelined.sync();
        } finally {
            jedis.close();
        }
    }

    @Override
    public List<byte[]> loadFileOnce(String fileDataKey, String fileName, long blockSize) {
        Jedis jedis = jedisPool.getResource();
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
                pipelined = jedis.pipelined();
                temps.clear();
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
        //hkeys.forEach(key -> names.add(new String(key, StandardCharsets.UTF_8)));
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
