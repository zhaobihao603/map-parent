package com.imap.cloud.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传工具类
 * @author 99901786
 *
 */
public class UploadUtils {
	final static SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
	
	private static String getExtName(MultipartFile file){		
		return FilenameUtils.getExtension(file.getOriginalFilename());
	}
	
	/**
	 * 保存到系统上的文件取名规则：yyyyMMddHHmmss+3为随机数+.+后缀名
	 * @param file	上传的文件
	 * @return
	 * @throws Exception 
	 **/
	private static String createNewName(MultipartFile file){
		String name = formatter.format(new Date());
		Random random = new Random();
        int rannum = (int) (random.nextDouble() * 900) + 100;// 获取3位随机数
		return new StringBuffer(name)
					.append(rannum)
					.append(".").append(getExtName(file)).toString();//file.getOriginalFilename();
	}
	
	/**
	 * 保存文件，并返回保存后文件的新名
	 * @name lcy
	 * @param file	上传的文件
	 * @param path  保存位置路径
	 * @return
	 * @throws Exception 
	 **/
	public static String upload(MultipartFile file,String path){
		String newName =null;
		try {
			//加密为新的文件名
			newName = createNewName(file);
			FileUtils.copyInputStreamToFile(file.getInputStream(), new File(path,newName));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return  newName;
	}
	
	
	/**
	 * 保存文件，并返回保存后文件的新名,多文件上传
	 * @name lcy
	 * @param file	上传的文件
	 * @param path  保存位置路径
	 * @return
	 **/
	public static String[] uploadarray(MultipartFile[] files,String path){
		String[] newNames = null;
		for (int i = 0; i < files.length; i++) {
			if(i==0){
				newNames = new String[files.length];
			}
			newNames[i] = upload(files[i], path);
		}
		return newNames;
	}
	
	
	
	/**
	 *  解压文件工具类
	 * @author lcy 
	 * @param zipFileName  解压文件名（全路径）
	 * @param extPlace     解压文件存储路径
	 * @throws Exception
	 */
    public static void unZipFiles(String zipFileName, String extPlace) throws Exception {
    	String fileUrl = "";
        try {  
        	File file = new File(extPlace);
        	if(!file.exists()){
        		file.mkdirs();
    		}
            File f = new File(zipFileName);
            
            ZipFile zipFile = new ZipFile(zipFileName);  //处理中文文件名乱码的问题
            long startDate=System.currentTimeMillis();
            long endDate=0L;
            System.out.println("开始解压文件..."+startDate);
            if((!f.exists()) && (f.length() <= 0)) {  
                throw new Exception("要解压的文件不存在!");  
            }  
            String strPath, gbkPath, strtemp;  
            File tempFile = new File(extPlace);  
            strPath = tempFile.getAbsolutePath();  
            Enumeration<?> e = zipFile.entries();
            while(e.hasMoreElements()){
                ZipEntry zipEnt = (ZipEntry) e.nextElement();
                gbkPath=zipEnt.getName();
                if(zipEnt.isDirectory()){  
                    strtemp = strPath + File.separator + gbkPath;  
                    File dir = new File(strtemp);  
                    dir.mkdirs();  
                    continue;  
                } else {
                    //读写文件  
                    InputStream is = zipFile.getInputStream(zipEnt);  
                    BufferedInputStream bis = new BufferedInputStream(is);  
                    gbkPath=zipEnt.getName();  
                    strtemp = strPath + File.separator + gbkPath;  
                  
                    //建目录  
                    String strsubdir = gbkPath;  
                    for(int i = 0; i < strsubdir.length(); i++) {  
                        if(strsubdir.substring(i, i + 1).equalsIgnoreCase("/")) {  
                            String temp = strPath + File.separator + strsubdir.substring(0, i);  
                            File subdir = new File(temp);  
                            if(!subdir.exists())  
                            subdir.mkdir();  
                        }  
                    }  
                    FileOutputStream fos = new FileOutputStream(strtemp);  
                    BufferedOutputStream bos = new BufferedOutputStream(fos);  
                    int c;  
                    while((c = bis.read()) != -1) {  
                        bos.write((byte) c);  
                    }
                    bos.close();
                    fos.close();
                    bis.close();
                    is.close();
                }
            }
            zipFile.close();
            endDate=System.currentTimeMillis();
            System.out.println("完成解压文件..."+endDate+"---解压所用时间:"+(endDate-startDate)+"毫秒");
        } catch(Exception e) {  
            e.printStackTrace(); 
        }
    }
	
	/**
	 * 文件下载工具类
	 * @name  lcy
	 * @param path 下载文件地址
	 * @param request
	 * @param response 返回下载文件名
	 * @return
	 * @throws IOException
	 */
    public static ResponseEntity<byte[]> download(String path,HttpServletRequest request,HttpServletResponse response) throws IOException {    
        File file=new File(path);  
        String[] paths=path.replaceAll("\\\\", "/").split("/");
        String table=null;
        for(int i = 0; i < paths.length; i++){
           if (i==paths.length-1) {
        	   table=paths[i];
		}
        }
        HttpHeaders headers = new HttpHeaders();    
        //解决浏览器中文下载中文乱码问题
        response.setHeader("content-disposition", String.format("attachment;filename*=utf-8'zh_cn'%s",URLEncoder.encode(table, "utf-8")));
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);   
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),    
                                          headers, HttpStatus.CREATED);  
    }    
	
}
