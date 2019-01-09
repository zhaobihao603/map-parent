package com.imap.cloud.common.cache.lucene.util;


public class FileBlocksUtils {
    public static byte[] getBlockName(String fileName, int i) {
        return String.format("@%s:%s", fileName, i).getBytes();
    }

    public static Long getBlockSize(long length) {
        return getBlockSize(length, Constants.BUFFER_SIZE);
    }

    private static Long getBlockSize(long length, long bufferSize) {
        if (bufferSize == 0) {
            //log.error("Default buffer size is zero!");
            return 0L;
        }
        return length % bufferSize == 0 ? length / bufferSize : length / bufferSize + 1;
    }
}
