package com.imap.cloud.common.util;

import java.io.Serializable;

/**
 * <strong>类概要： FastDFS文件实体</strong> <br>
 * 
 * @author Hxin
 * @version 1.0.0
 */
public interface FileManagerConfig extends Serializable{

    public static final String FILE_DEFAULT_AUTHOR = "imap";

    public static final String PROTOCOL = "http://";

    public static final String SEPARATOR = "/";

    public static final String TRACKER_NGNIX_ADDR = "192.168.28.128";

    public static final String TRACKER_NGNIX_PORT = "";

    public static final String CLIENT_CONFIG_FILE = "fdfs_client.conf";
}
