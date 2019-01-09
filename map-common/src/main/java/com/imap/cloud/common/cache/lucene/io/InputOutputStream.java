package com.imap.cloud.common.cache.lucene.io;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import com.imap.cloud.common.cache.lucene.Operations;

public interface InputOutputStream {
    /**
     * @param key   first field
     * @param field second field
     * @return iff exists return true
     */
    Boolean hexists(final byte[] key, final byte[] field);

    byte[] hget(final byte[] key, final byte[] field, Operations operations);

    void close() throws IOException;

    Long hdel(final byte[] key, final byte[]... fields);

    Long hset(final byte[] key, final byte[] field, final byte[] value, Operations operations);

    Set<byte[]> hkeys(final byte[] key);

    String[] getAllFileNames(String directoryMedata);

    /**
     * Use transactions to delete index file
     *
     * @param fileLengthKey the key using for hash file length
     * @param fileDataKey   the key using for hash file data
     * @param field         the hash field
     * @param blockSize     the index file data block size
     */
    void deleteFile(String fileLengthKey, String fileDataKey, String field, long blockSize);

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
    void rename(String fileLengthKey, String fileDataKey, String oldField, String newField, List<byte[]> values, long
            fileLength);

    void checkTransactionResult(List<Object> exec);

    void saveFile(String fileLengthKey, String fileDataKey, String fileName, List<byte[]> values, long fileLength);

    List<byte[]> loadFileOnce(String fileDataKey, String fileName, long blockSize);
}
