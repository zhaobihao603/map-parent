package com.imap.cloud.common.cache.lucene.util;

import com.google.common.base.Strings;

import java.util.ResourceBundle;

public class ConfigUtils {
    private ConfigUtils() {
    }

    //default config file name
    private static ResourceBundle resourceBundle = ResourceBundle.getBundle("config");

    public static void setResourceBundle(String fileName) {
        resourceBundle = ResourceBundle.getBundle(fileName);
    }

    /**
     * @param key key in config file
     * @return value
     */
    public static String getValue(String key) {
        boolean nullOrEmpty = Strings.isNullOrEmpty(key);
        String res = null;
        if (!nullOrEmpty && resourceBundle != null) {
            res = resourceBundle.getString(key);
            //log.debug("key = " + key + ", value = " + res);
        } else {
            //log.error("Key or resource bundle is null or empty!");
        }
        return res;
    }
}

