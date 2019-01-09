package com.imap.cloud.common.cache.lucene.io;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.imap.cloud.common.cache.lucene.Operations;

import redis.clients.jedis.Response;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPipeline;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

import com.google.common.collect.Lists;
import com.google.common.primitives.Longs;
import static com.imap.cloud.common.cache.lucene.util.CompressUtils.compressFilter;
import static com.imap.cloud.common.cache.lucene.util.CompressUtils.uncompressFilter;
import static com.imap.cloud.common.cache.lucene.util.FileBlocksUtils.getBlockName;
import static com.imap.cloud.common.cache.lucene.util.FileBlocksUtils.getBlockSize;
import com.imap.cloud.common.cache.lucene.util.Constants;

public class ShardedJedisPoolStream implements InputOutputStream {
    private ShardedJedisPool shardedJedisPool;

    private ShardedJedis getShardedJedis() {
        return shardedJedisPool.getResource();
    }

    public ShardedJedisPoolStream(ShardedJedisPool shardedJedisPool) {
        this.shardedJedisPool = shardedJedisPool;
    }

    @Override
    public Boolean hexists(byte[] key, byte[] field) {
        ShardedJedis shardedJedis = getShardedJedis();
        boolean hexists = shardedJedis.hexists(key, field);
        shardedJedis.close();
        return hexists;
    }

    @Override
    public byte[] hget(byte[] key, byte[] field, Operations operations) {
        ShardedJedis shardedJedis = getShardedJedis();
        byte[] hget = shardedJedis.hget(key, field);
        shardedJedis.close();
        if (operations == Operations.FILE_DATA) {
            return uncompressFilter(hget);
        }
        return hget;
    }

    @Override
    public void close() throws IOException {
        if (shardedJedisPool != null) {
            shardedJedisPool.close();
        }
    }

    @Override
    public Long hdel(byte[] key, byte[]... fields) {
        ShardedJedis shardedJedis = getShardedJedis();
        Long hdel = shardedJedis.hdel(key, fields);
        return hdel;
    }

    @Override
    public Long hset(byte[] key, byte[] field, byte[] value, Operations operations) {
        ShardedJedis shardedJedis = getShardedJedis();
        if (operations == Operations.FILE_DATA) {
            value = compressFilter(value);
        }
        Long hset = shardedJedis.hset(key, field, value);
        shardedJedis.close();
        return hset;
    }

    @Override
    public Set<byte[]> hkeys(byte[] key) {
        ShardedJedis shardedJedis = getShardedJedis();
        Set<byte[]> hkeys = shardedJedis.hkeys(key);
        shardedJedis.close();
        return hkeys;
    }

    /**
     * Use transactions to delete index file
     *
     * @param fileLengthKey the key using for hash file length
     * @param fileDataKey   the key using for hash file data
     * @param field         the hash field
     * @param blockSize     the index file data block size
     */
    @Override
    public void deleteFile(String fileLengthKey, String fileDataKey, String field, long blockSize) {
        ShardedJedis shardedJedis = getShardedJedis();
        ShardedJedisPipeline pipelined = shardedJedis.pipelined();
        //delete file length
        pipelined.hdel(fileLengthKey.getBytes(), field.getBytes());
        //delete file content
        for (int i = 0; i < blockSize; i++) {
            byte[] blockName = getBlockName(field, i);
            pipelined.hdel(fileDataKey.getBytes(), blockName);
        }
        pipelined.sync();
        shardedJedis.close();
    }

    /**
     * Use transactions to add index file and then delete the old one
     *
     * @param fileLengthKey the key using for hash file length
     * @param fileDataKey   the key using for hash file data
     * @param oldField      the old hash field
     * @param newField      the new hash field
     * @param values        the data values of the old hash field
     * @param fileLength    the data length of the old hash field
     */
    @Override
    public void rename(String fileLengthKey, String fileDataKey, String oldField, String newField, List<byte[]> values, long
            fileLength) {
        ShardedJedis shardedJedis = getShardedJedis();
        ShardedJedisPipeline pipelined = shardedJedis.pipelined();
        //add new file length
        pipelined.hset(fileLengthKey.getBytes(), newField.getBytes(), Longs.toByteArray(fileLength));
        //add new file content
        Long blockSize = getBlockSize(fileLength);
        for (int i = 0; i < blockSize; i++) {
            pipelined.hset(fileDataKey.getBytes(), getBlockName(newField, i), compressFilter(values.get(i)));
        }
        pipelined.sync();
        shardedJedis.close();
        values.clear();
        deleteFile(fileLengthKey, fileDataKey, oldField, blockSize);
    }

    @Override
    public void saveFile(String fileLengthKey, String fileDataKey, String fileName, List<byte[]> values, long fileLength) {
        ShardedJedis shardedJedis = getShardedJedis();
        ShardedJedisPipeline pipelined = shardedJedis.pipelined();
        pipelined.hset(fileLengthKey.getBytes(), fileName.getBytes(), Longs.toByteArray(fileLength));
        Long blockSize = getBlockSize(fileLength);
        for (int i = 0; i < blockSize; i++) {
            pipelined.hset(fileDataKey.getBytes(), getBlockName(fileName, i), compressFilter(values.get(i)));
            if (i % Constants.SYNC_COUNT == 0) {
                pipelined.sync();
                pipelined = shardedJedis.pipelined();
            }
        }
        pipelined.sync();
        shardedJedis.close();
        values.clear();
    }

    @Override
    public List<byte[]> loadFileOnce(String fileDataKey, String fileName, long blockSize) {
        ShardedJedis shardedJedis = getShardedJedis();
        ShardedJedisPipeline pipelined = shardedJedis.pipelined();
        List<byte[]> res = new ArrayList<>();
        List<Response<byte[]>> temps = new ArrayList<>();
        int temp = 0;
        //如果不分批次sync容易read time out和Java heap space
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
                pipelined = shardedJedis.pipelined();
            }
            temp++;
        }
        try {
            pipelined.sync();
        } catch (JedisConnectionException e) {
            //log.error("pipelined = {}, blockSize = {}!", pipelined.toString(), blockSize);
            //log.error("", e);
        } finally {
            shardedJedis.close();
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