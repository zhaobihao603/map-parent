package com.imap.cloud.common.service.vr.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.imap.cloud.common.aop.annotation.LogDescription;
import com.imap.cloud.common.dao.vr.VrMapper;
import com.imap.cloud.common.dto.BaseResult;
import com.imap.cloud.common.entity.vr.Vr;
import com.imap.cloud.common.enums.Module;
import com.imap.cloud.common.service.base.impl.BaseServiceImpl;
import com.imap.cloud.common.service.system.FileService;
import com.imap.cloud.common.service.vr.VrService;
import com.imap.cloud.common.util.AccountUtils;

@LogDescription(name="VR服务")
@Service
public class VrServiceImpl extends BaseServiceImpl<Vr, String> implements VrService {

	
	private VrMapper vrmapper;
	@Autowired
	private FileService fileService;
	
	String image=null;
	String vrUrl=null;
	
	@Autowired
	VrServiceImpl(VrMapper vrmapper){
		this.vrmapper =  vrmapper;
		super.setBaseDao(vrmapper);
	}
	
	@Override
	@LogDescription(name="vr删除记录")
	@Transactional
	public int deleteByPrimaryKey(String id) {
		return vrmapper.deleteByPrimaryKey(id);
	}


	@Override
	public Vr selectByPrimaryKey(String id) {
		return vrmapper.selectByPrimaryKey(id);
	}
	
	public List<Vr> selectList(Vr vr) throws Exception{
		return vrmapper.selectAll(vr);
	}

	@Override
	@LogDescription(name="vr修改记录")
	@Transactional
	public BaseResult<T> update(Vr vr,String upid,String title,HttpServletRequest request){
		//创建一个通用的多部分解析器.  
        CommonsMultipartResolver cmr = new CommonsMultipartResolver(request.getSession().getServletContext());  
        //设置编码  
        cmr.setDefaultEncoding("utf-8");
        if (cmr.isMultipart(request)) {  
        	MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;    
            List<MultipartFile> fileList = multipartRequest.getFiles("files");
            String image = null;
            String url = null;
            for(int i=0;fileList!=null && i<fileList.size();i++){
        		String ext = FilenameUtils.getExtension(fileList.get(i).getOriginalFilename()).toLowerCase();
        		if(image!=null && url != null) break;
            	String filePath = fileService.uploadFile(fileList.get(i), Module.VR.getCode(), request);
            	String fileId = fileService.save(fileList.get(i), filePath, Module.VR.getCode(), title, null, multipartRequest);
            	if(ext.equals("jpg")||ext.equals("jpeg")||ext.equals("png")
            			||ext.equals("bmp")||ext.equals("gif")){
            		image = filePath;
            		vrmapper.linkVrUpload(upid, fileId);
            	}else{
            		url = filePath;
            		//vrmapper.deleteVrUpload(upid);
            		vrmapper.linkVrUpload(upid, fileId);
            	}
            }
        }
        vr.setId(upid);
		vr.setImageurl(image);
		vr.setVruploadUrl(vrUrl);
		int num=vrmapper.updateByPrimaryKeySelective(vr);
		return new BaseResult<T>(true, num, "成功");
	}
	
	@Override
	@LogDescription(name="vr插入记录")
	@Transactional
	public int insert(Vr vr,String title,HttpServletRequest request) {
		//创建一个通用的多部分解析器.  
        CommonsMultipartResolver cmr = new CommonsMultipartResolver(request.getSession().getServletContext());  
        //设置编码  
        cmr.setDefaultEncoding("utf-8");
        String id = AccountUtils.getUUID();
        if (cmr.isMultipart(request)) {  
        	MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;    
            List<MultipartFile> fileList = multipartRequest.getFiles("files");
            String image = null;
            String url = null;
            if(fileList!=null && fileList.size()>0){
            	for(MultipartFile f :fileList){
            		String ext = FilenameUtils.getExtension(f.getOriginalFilename()).toLowerCase();
            		if(image!=null && url != null) break;
	            	String filePath = fileService.uploadFile(f, Module.VR.getCode(), request);
	            	String fileId = fileService.save(f, filePath, Module.VR.getCode(), title, null, multipartRequest);
	            	if(ext.equals("jpg")||ext.equals("jpeg")||ext.equals("png")
	            			||ext.equals("bmp")||ext.equals("gif")){
	            		image = filePath;
	            		vrmapper.linkVrUpload(id, fileId);
	            	}else{
	            		url = filePath;
	            		vrmapper.linkVrUpload(id, fileId);
	            	}
            	}
            }
        }
        vr.setId(id);
        vr.setImageurl(image);
		vr.setVruploadUrl(vrUrl);
		int num=vrmapper.insert(vr);
		return num;
	}
}	
