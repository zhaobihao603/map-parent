package com.imap.cloud.common.util;

import java.io.File;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class FileManager implements FileManagerConfig {
	private static final long serialVersionUID = 1L;
    private static TrackerClient trackerClient;
    private static TrackerServer trackerServer;
    private static StorageServer storageServer;
    private static StorageClient storageClient;

    static {
        try {
            //String classPath = new File(FileManager.class.getResource("/").getFile()).getCanonicalPath();
            String configPath = FileManager.class.getClassLoader().getResource(CLIENT_CONFIG_FILE).getFile();
            System.out.println("==================================================================================");
            System.out.println(FileManager.class.getResource("/"+CLIENT_CONFIG_FILE));
            System.out.println(FileManager.class.getClassLoader().getResource(CLIENT_CONFIG_FILE).getFile());
            //String fdfsClientConfigFilePath = classPath + File.separator + CLIENT_CONFIG_FILE;
            ClientGlobal.init(configPath);//fdfsClientConfigFilePath);

            trackerClient = new TrackerClient();
            trackerServer = trackerClient.getConnection();

            storageClient = new StorageClient(trackerServer, storageServer);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * <strong>方法概要： 文件上传</strong> <br>
     * 
     * @param FastDFSFile file
     * @return fileAbsolutePath
     */
    public static String upload(FastDFSFile file,NameValuePair[] valuePairs) {
        String[] uploadResults = null;
        try {
            uploadResults = storageClient.upload_file(file.getContent(),file.getExt(), valuePairs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String groupName = uploadResults[0];
        String remoteFileName = uploadResults[1];

        String fileAbsolutePath = PROTOCOL
                + TRACKER_NGNIX_ADDR
                //+ trackerServer.getInetSocketAddress().getHostName()
                //+ SEPARATOR + TRACKER_NGNIX_PORT 
                + SEPARATOR + groupName
                + SEPARATOR + remoteFileName;
        return fileAbsolutePath;
    }
    
    /**
     * <strong>方法概要： 文件下载</strong> <br>
     * 
     * @param String groupName
     * @return fileAbsolutePath
     */
    public static ResponseEntity<byte[]> download(String groupName,
            String remoteFileName,String specFileName) {
        byte[] content = null;
        HttpHeaders headers = new HttpHeaders();
        try {
            content = storageClient.download_file(groupName, remoteFileName);
            headers.setContentDispositionFormData("attachment",  new String(specFileName.getBytes("UTF-8"),"iso-8859-1"));
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<byte[]>(content, headers, HttpStatus.CREATED);
    }

}
