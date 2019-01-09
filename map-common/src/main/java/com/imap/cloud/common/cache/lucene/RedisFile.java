package com.imap.cloud.common.cache.lucene;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


import org.apache.lucene.util.Accountable;

public class RedisFile  implements Accountable {
    private List<byte[]> buffers = new ArrayList<>();
    private String fileName;
    private byte[] filePath;
    public List<byte[]> getBuffers() {
		return buffers;
	}

	public void setBuffers(List<byte[]> buffers) {
		this.buffers = buffers;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public byte[] getFilePath() {
		return filePath;
	}

	public void setFilePath(byte[] filePath) {
		this.filePath = filePath;
	}

    private long fileLength;
    private volatile boolean isDirty;
    protected long sizeInBytes;

    public RedisFile(String fileName, long fileLength) {
        this.fileName = fileName;
        this.fileLength = fileLength;
    }

    public RedisFile() {
		
	}

	/**
     * Return the memory usage of this object in bytes. Negative values are illegal.
     */
    @Override
    public long ramBytesUsed() {
        return sizeInBytes;
    }

    /**
     * Returns nested resources of this class.
     * The result should be a point-in-time snapshot (to avoid race conditions).
     *
     * @see org.apache.lucene.util.Accountables
     */
    @Override
    public Collection<Accountable> getChildResources() {
        return Collections.emptyList();
    }

    public final byte[] addBuffer(int size) {
        byte[] buffer = newBuffer(size);
        synchronized (this) {
            buffers.add(buffer);
            sizeInBytes += size;
        }
        RedisDirectory.getSizeInBytes().getAndAdd(size);
        return buffer;
    }

    private byte[] newBuffer(int size) {
        return new byte[size];
    }

    public final synchronized byte[] getBuffer(int index) {
        return buffers.get(index);
    }

    public final synchronized int numBuffers() {
        return buffers.size();
    }

    public synchronized long getFileLength() {
        return fileLength;
    }

    public synchronized void setLength(long length) {
        this.fileLength = length;
    }
}
