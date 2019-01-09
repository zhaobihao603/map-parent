package com.imap.cloud.common.cache.lucene.util;

import java.io.IOException;

import org.xerial.snappy.Snappy;

public class CompressUtils {
    public static byte[] compressFilter(byte[] datas) {
        if (Constants.COMPRESS_FILE) {
            try {
                datas = Snappy.compress(datas);
            } catch (IOException e) {
                //log.error("Compress error!", e);
            }
        }
        return datas;
    }

    public static byte[] uncompressFilter(byte[] datas) {
        if (Constants.COMPRESS_FILE) {
            try {
                datas = Snappy.uncompress(datas);
            } catch (IOException e) {
                //log.error("Uncompress error!", e);
            }
        }
        return datas;
    }
}
