package com.imap.cloud.common.cache.lucene;

import static com.imap.cloud.common.cache.lucene.Operations.FILE_DATA;
import static com.imap.cloud.common.cache.lucene.Operations.FILE_LENGTH;
import static com.imap.cloud.common.cache.lucene.util.FileBlocksUtils.getBlockName;
import static com.imap.cloud.common.cache.lucene.util.FileBlocksUtils.getBlockSize;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.lucene.store.BaseDirectory;
import org.apache.lucene.store.IOContext;
import org.apache.lucene.store.IndexInput;
import org.apache.lucene.store.IndexOutput;
import org.apache.lucene.util.Accountable;
import org.apache.lucene.util.Accountables;

import com.google.common.primitives.Longs;
import com.imap.cloud.common.cache.lucene.io.InputOutputStream;
import com.imap.cloud.common.cache.lucene.io.RedisInputStream;
import com.imap.cloud.common.cache.lucene.io.RedisOutputStream;
import com.imap.cloud.common.cache.lucene.util.Constants;


public class RedisDirectory extends BaseDirectory implements Accountable {
	private InputOutputStream inputOutputStream;
	//key is index file name
    private static volatile Map<String, RedisFile> filesMap = new ConcurrentHashMap<>();
    
    private static final AtomicLong sizeInBytes = new AtomicLong();

	protected RedisDirectory() throws IOException {
//		super(lockFactory);
		super(new RedisLockFactory());
	}
	
	public RedisDirectory(InputOutputStream inputOutputStream) throws IOException {
        this();
        this.inputOutputStream = inputOutputStream;
    }
	
	/**
     * @return get all the file names lists
     */
    @Override
    public final String[] listAll() {
        ensureOpen();
        //directory->fileNames->fileLength，由fileLength%BLOCK_SIZE==0?fileLength/BLOCK_SIZE:fileLength/BLOCK_SIZE+1得到fileBlockSizes
        return inputOutputStream.getAllFileNames(Constants.DIRECTORY_METADATA);
    }

    /**
     * Returns true iff the named file exists in this directory.
     */
    private boolean fileNameExists(String fileName) {
        return inputOutputStream.hexists(Constants.DIR_METADATA_BYTES, fileName.getBytes());
    }

    /**
     * @param name file name
     * @return Returns the length of a file in the directory.
     * @throws IOException an I/O error
     */
    @Override
    public final long fileLength(String name) throws IOException {
        ensureOpen();
        long current = 0;
        byte[] b = inputOutputStream.hget(Constants.DIR_METADATA_BYTES, name.getBytes(), FILE_LENGTH);
        if (b != null) {
            current = Longs.fromByteArray(b);
        }
        return current;
    }

    @Override
    public void deleteFile(String name) throws IOException {
        ensureOpen();
        boolean b = fileNameExists(name);
        if (b) {
            byte[] hget = inputOutputStream.hget(Constants.DIR_METADATA_BYTES, name.getBytes(), FILE_LENGTH);
            long length = Longs.fromByteArray(hget);
            long blockSize = getBlockSize(length);
            inputOutputStream.deleteFile(Constants.DIRECTORY_METADATA, Constants.FILE_METADATA, name, blockSize);
        } else {
            //log.error("Delete file {} does not exists!", name);
        }
    }

    /**
     * Creates a new, empty file in the directory with the given name. Returns a stream writing this file.
     */
    @Override
    public IndexOutput createOutput(String name, IOContext context) throws IOException {
        ensureOpen();
        return new RedisOutputStream(name, getInputOutputStream());
    }

    @Override
    public void sync(Collection<String> names) throws IOException {
        //NOOP, no operation
    }

    @Override
    public void renameFile(String source, String dest) throws IOException {
        List<byte[]> values = new ArrayList<>();
        //在get的时候不需要加事务
        //在删除和添加的时候使用事务
        //Get the file length with old file name
        byte[] hget = inputOutputStream.hget(Constants.DIR_METADATA_BYTES, source.getBytes(), FILE_LENGTH);
        long length = Longs.fromByteArray(hget);
        long blockSize = getBlockSize(length);
        for (int i = 0; i < blockSize; i++) {
            //Get the contents with old file name
            byte[] res = inputOutputStream.hget(Constants.FILE_METADATA_BYTES, getBlockName(source, i), FILE_DATA);
            values.add(res);
        }
        inputOutputStream.rename(Constants.DIRECTORY_METADATA, Constants.FILE_METADATA, source, dest, values, length);
        //log.debug("Rename file success from {} to {}", source, dest);
    }

    @Override
    public IndexInput openInput(String name, IOContext context) throws IOException {
        ensureOpen();
        if (!fileNameExists(name)) {
            throw new FileNotFoundException(name);
        }
        //从redis中load文件到redis file对象中
        //单例Jedis有一个坑，就是在同一时刻只能被一个线程持有，在openInput方法中，Lucene会有Read操作和Merge操作，而其由不同的线程完成，所以如果在
        //loadRedisToFile中出现不同线程在瞬时同时持有Jedis对象会一直报错Socket Closed
        return new RedisInputStream(name, loadRedisToFile(name));
    }

    private RedisFile loadRedisToFile(String fileName) {
        byte[] hget = inputOutputStream.hget(Constants.DIR_METADATA_BYTES, fileName.getBytes(), FILE_LENGTH);
        long lenght = Longs.fromByteArray(hget);
        RedisFile redisFile = new RedisFile(fileName, lenght);
        long blockSize = getBlockSize(lenght);
        List<byte[]> bytes = inputOutputStream.loadFileOnce(Constants.FILE_METADATA, fileName, blockSize);
        redisFile.setBuffers(bytes);
        return redisFile;
    }

    @Override
    public void close() throws IOException {
        isOpen = false;
        inputOutputStream.close();
    }

    /**
     * Return the memory usage of this object in bytes. Negative values are illegal.
     */
    @Override
    public long ramBytesUsed() {
        ensureOpen();
        return sizeInBytes.get();
    }
    
    /**
     * Returns nested resources of this class.
     * The result should be a point-in-time snapshot (to avoid race conditions).
     *
     * @see Accountables
     */
    @Override
    public Collection<Accountable> getChildResources() {
        return Collections.emptyList();
    }
    

	public static Map<String, RedisFile> getFilesMap() {
		return filesMap;
	}

	public InputOutputStream getInputOutputStream() {
		return inputOutputStream;
	}


	public static AtomicLong getSizeInBytes() {
		return sizeInBytes;
	}

}
