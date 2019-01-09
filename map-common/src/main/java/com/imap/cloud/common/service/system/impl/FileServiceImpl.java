package com.imap.cloud.common.service.system.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.servlet.http.HttpServletRequest;

import com.imap.cloud.common.enums.Module;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.csource.common.NameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.druid.util.StringUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.imap.cloud.common.aop.annotation.LogDescription;
import com.imap.cloud.common.cache.RedisCache;
import com.imap.cloud.common.dao.system.SysFileMapper;
import com.imap.cloud.common.dto.Pager;
import com.imap.cloud.common.entity.security.User;
import com.imap.cloud.common.entity.system.SysFile;
import com.imap.cloud.common.service.system.FileService;
import com.imap.cloud.common.util.FastDFSFile;
import com.imap.cloud.common.util.FileManager;

@LogDescription(name="系统文件的服务")
@Service
public class FileServiceImpl implements FileService{
	final static SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
	@Autowired
	private SysFileMapper fileMapper;
	
	@Autowired
	private RedisCache cache;
	
	@Value(value="${usedFastDFS}")
	private Boolean usedFastDFS;
	@Value(value="${fastdfsServer}")
	private String fastdfsServer;
	
	public SysFile load(String id){
		return fileMapper.load(id);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Pager<SysFile> search(Map conditionMap,String[] fileFormats,Integer pageSize, Integer pageNum){
		Page<SysFile> page = PageHelper.startPage(pageNum==null?1:pageNum,pageSize==null?10:pageSize,true);
		List lst  = new ArrayList();
		for (int i = 0; i < fileFormats.length; i++) {
			lst.add(fileFormats[i]);
		}
		conditionMap.put("fileFormats",lst);
		Object moduleObj = conditionMap.get("module");
		String module = Module.getCodeByName(String.valueOf(moduleObj));
		if (module!=null){
			conditionMap.put("module",module);
		}
		List<SysFile> pageList = fileMapper.select(conditionMap);
		return new Pager(pageList,page);
	}
	
	public boolean update(String id,String filePath,String module,String title,String description,Long fileSize){
		SysFile f = new SysFile();
		f.setId(id);
		f.setRealName(filePath==null?null:FilenameUtils.getName(filePath).toLowerCase());
		f.setFileSize(fileSize);
		f.setFileFormat(filePath==null?null:FilenameUtils.getExtension(filePath).toLowerCase());
		f.setDeleted(false);
		f.setModule(module);
		f.setTitle(title);
		f.setDescription(description);
		f.setFileUrl(filePath);
		int num = fileMapper.update(f);
		return num>0;
	}
	
	/**
	 * 记录上传记录到数据库里
	 * 返回数据库里文件记录的主键id
	 * return
	 */
	public String save(MultipartFile attach,String filePath,String module, String title,String description,HttpServletRequest request){
		SysFile f = new SysFile();
		User user = (User)request.getSession().getAttribute("user");
		String uuid = UUID.randomUUID().toString().replace("-", "");
		f.setFileName(attach.getOriginalFilename());
		f.setRealName(FilenameUtils.getName(filePath).toLowerCase());
		f.setFileFormat(FilenameUtils.getExtension(filePath).toLowerCase());
		f.setFileSize(attach.getSize());
		f.setDeleted(false);
		f.setModule(module);
		f.setUploadDate(new Date());
		f.setUploader(user==null?null:user.getId());
		f.setTitle(title);
		f.setDescription(description);
		f.setFileUrl(filePath);
		f.setId(uuid);
		fileMapper.save(f);
		return uuid;
	}
	
	/**
	 * 上传文件到文件服务器
	 * 返回文件在服务器上的路径
	 * return 
	 */
	public String uploadFile(MultipartFile attach,String module,HttpServletRequest request){
		String ext = FilenameUtils.getExtension(attach.getOriginalFilename()).toLowerCase();
		try{
			String filePath = null;
			if(usedFastDFS==null ||!usedFastDFS){
				String rootPath = request.getSession().getServletContext().getRealPath("/");
				String modulePath = "/upload/"+module;
				//保存到系统上的文件取名规则：yyyyMMddHHmmss+3为随机数+.+后缀名
				String name = formatter.format(new Date());
				Random random = new Random();
		        int rannum = (int) (random.nextDouble() * 900) + 100;// 获取3位随机数
		        String fileName = new StringBuffer(name).append(rannum).append(".").append(ext).toString();
		        FileUtils.copyInputStreamToFile(attach.getInputStream(), new File(rootPath+modulePath,fileName));
		        filePath = modulePath+"/"+fileName;//相对路径
			}else{
				FastDFSFile file = new FastDFSFile(attach.getBytes(),ext);
				NameValuePair[] meta_list = new NameValuePair[4];
				meta_list[0] = new NameValuePair("fileName", attach.getOriginalFilename());
				meta_list[1] = new NameValuePair("fileLength", String.valueOf(attach.getSize()));
				meta_list[2] = new NameValuePair("fileExt", ext);
				meta_list[3] = new NameValuePair("fileAuthor", "SMART_IMAP");
				filePath = FileManager.upload(file,meta_list);
				filePath = fastdfsServer+File.separatorChar+filePath.substring(filePath.indexOf("group"));
			}
			return filePath;
		}catch(IOException e){
			e.printStackTrace();
		}		
		return null;
	}
	
	/**
	 * 上传文件，并记录到数据库里
	 * 返回数据库里文件记录的主键id
	 * return
	 */
	public String upload(MultipartFile attach,String module,HttpServletRequest request){
		String filePath = uploadFile(attach,module,request);
		return this.save(attach, filePath, module, null, null, request);//uuid;
	}
	
	public String[] upload(MultipartFile[] attachs,String module,HttpServletRequest request){
		String[] ids = new String[attachs.length];
		for(int i=0;i<attachs.length;i++){
			ids[i] = upload(attachs[i], module,request);
		}
		return ids;
	}
	
	public ResponseEntity<byte[]> download(String id,HttpServletRequest request) throws Exception{
		SysFile file = load(id);
		if(file==null) throw new Exception("文件找不到。");
		if(usedFastDFS==null || !usedFastDFS){
			String rootPath = request.getSession().getServletContext().getRealPath("/");
			File f =new File(rootPath+file.getFileUrl()); 
			HttpHeaders headers = new HttpHeaders();
	        //解决浏览器中文下载中文乱码问题
			headers.setContentDispositionFormData("attachment",  new String(file.getFileName().getBytes("UTF-8"),"iso-8859-1"));
	        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);   
	        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(f),headers, HttpStatus.CREATED);
		}else{
			String filePath = file.getFileUrl();
		    String substr = filePath.substring(filePath.indexOf("group"));
		    String group = substr.split("/")[0];
		    String remoteFileName = substr.substring(substr.indexOf("/")+1);
		    String specFileName = file.getFileName() + substr.substring(substr.indexOf("."));
		    return FileManager.download(group, remoteFileName,specFileName);
		}
	}
	
	public static void main(String[] args) {
		String filePath = "http://192.168.7.128/group1/M00/00/00/wKgcgFjPtj2AMq8QAACI8g2OVXY489.png";
		System.out.println(filePath.substring(filePath.indexOf("group")));
	}
	
	public List<SysFile> getScenicPhoto(String scenicId){
		return fileMapper.selectScenicPhoto(scenicId);
	}
	
	public List<SysFile> getByModule(String name){
		return fileMapper.getByModule(name);
	}
	
	public List<SysFile> selectByIds(String[] lst){
		return fileMapper.selectByIds(lst);
	}
	
	/**
     * 把VR压缩包
     * @param path 文件路径
     * @param dir  指定目录
     * @throws Exception
     * @throws Exception
     */
	public void unzipVR(String path,String dir,HttpServletRequest request) throws Exception{
		String rootPath = request.getSession().getServletContext().getRealPath("/");
		if(StringUtils.isEmpty(dir)) dir = rootPath+"vr";
    	FileInputStream fis = new FileInputStream(path);
    	ZipInputStream zipInput = new ZipInputStream(fis,Charset.forName("gbk")); 
    	
    	BufferedInputStream bis = new BufferedInputStream(zipInput);
         
        File outFile = null;
        ZipEntry entry = null;
        BufferedOutputStream bos = null;
        boolean valid = false;
        while ((entry = zipInput.getNextEntry()) != null) {
            outFile = new File(dir,entry.getName());
            if(entry.isDirectory()){
            	if(outFile.getName().toLowerCase().equals("images") && outFile.exists()){
            		outFile = new File(dir,outFile.getName());
            		outFile.mkdir();//D:/zhijiang/images
            	}
            	continue;
            }
            if(outFile.getParentFile().getName().equals("images")
            		&&(outFile.getName().toLowerCase().endsWith("png") ||
            				outFile.getName().toLowerCase().endsWith("jpg")||
            				outFile.getName().toLowerCase().endsWith("gif"))){
            	outFile = new File(dir+"/images",outFile.getName());
            }else if(outFile.getName().toLowerCase().endsWith("html")){
            	outFile = new File(dir,"index.html");
            	valid = true;
            }else if(outFile.getName().toLowerCase().endsWith("js")||
    				outFile.getName().toLowerCase().endsWith("xml")){
            	outFile = new File(dir,outFile.getName());
            }else{
            	//throw new Exception("无法识别的VR压缩文件！");
            	continue;
            }
            if(!outFile.getParentFile().exists()){  
                outFile.getParentFile().mkdirs();  
            }
            if(!outFile.exists()){  
                outFile.createNewFile();  
            }
            
            bos=new BufferedOutputStream(new FileOutputStream(outFile));
            int temp = 0;  
            while((temp = bis.read())!= -1){     
            	bos.write(temp);  
            }  
            bos.close(); 
        }
        bis.close();
        fis.close();  
        zipInput.close();
        if(!valid){
        	throw new Exception("无法识别的VR压缩文件！"); 
        }
	}
}
