package com.imap.cloud.common.service.school.impl;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.imap.cloud.common.aop.annotation.LogDescription;
import com.imap.cloud.common.dao.school.SchoolactivityMapper;
import com.imap.cloud.common.dto.BaseResult;
import com.imap.cloud.common.entity.school.Schoolactivity;
import com.imap.cloud.common.enums.Module;
import com.imap.cloud.common.service.base.impl.BaseServiceImpl;
import com.imap.cloud.common.service.school.SchoolactivityService;
import com.imap.cloud.common.service.system.FileService;
import com.imap.cloud.common.util.AccountUtils;

@LogDescription(name="校园活动服务")
@Service
public class SchoolactivityServiceImpl extends BaseServiceImpl<Schoolactivity, String> implements SchoolactivityService {
	
	SchoolactivityMapper schoolmapper;
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	SchoolactivityServiceImpl(SchoolactivityMapper schoolmapper){
		this.schoolmapper =  schoolmapper;
		super.setBaseDao(schoolmapper);
	}
	
	@Override
	@LogDescription(name="活动删除记录")
	@Transactional
	public int deleteByPrimaryKey(String id) {
		return schoolmapper.deleteByPrimaryKey(id);
	}

	@Override
	public Schoolactivity selectByPrimaryKey(String id) {
		return schoolmapper.selectByPrimaryKey(id);
	}

	@Override
	@LogDescription(name="活动发布修改记录")
	@Transactional
	public int updateByfabu(String id, boolean fabu,Integer fabuState) {
		fabu=fabu?true:false;
		return schoolmapper.updateByfabu(id, fabu,fabuState);
	}

	public List<Schoolactivity> selectList(Schoolactivity entity) throws Exception {
        List<Schoolactivity> actList = schoolmapper.selectAll(entity);
		return actList;
	}

	@Override	
	@LogDescription(name="活动插入记录")
	@Transactional
	public int insert(Schoolactivity record,String title,HttpServletRequest request){
		CommonsMultipartResolver commonsMultipartResolver = new   
        		CommonsMultipartResolver(request.getSession().getServletContext());  
        commonsMultipartResolver.setDefaultEncoding("utf-8"); 
        String id = AccountUtils.getUUID();
        if (commonsMultipartResolver.isMultipart(request)) { 
        	MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request; 
        	List<MultipartFile> fileList = multipartRequest.getFiles("files");
        	for(int i =0;fileList!=null && i<fileList.size();i++){
        		String path = fileService.uploadFile(fileList.get(i), Module.ACTIVITY.getCode(), request);
            	String fileId = fileService.save(fileList.get(i),path,Module.ACTIVITY.getCode(), title, null,request);
            	schoolmapper.linkschoolUpload(id, fileId);
        	}
        }
		record.setDeleted(false);
		record.setCreatedTime(new Date());
		record.setUpdatedTime(new Date());
		record.setFabu(false);
		record.setFabuState(0);
		record.setId(id);
		int num=schoolmapper.insert(record);
		return num;
	}

	@Override
	@LogDescription(name="活动修改记录")
	@Transactional
	public BaseResult<T> update(Schoolactivity record,String upid,String title,HttpServletRequest request){
		CommonsMultipartResolver commonsMultipartResolver = new   
        		CommonsMultipartResolver(request.getSession().getServletContext());  
        commonsMultipartResolver.setDefaultEncoding("utf-8"); 
        if (commonsMultipartResolver.isMultipart(request)) { 
        	MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request; 
        	List<MultipartFile> fileList = multipartRequest.getFiles("files");
        	for(int i =0;fileList!=null && i<fileList.size();i++){
        		String path = fileService.uploadFile(fileList.get(i), Module.ACTIVITY.getCode(), request);
            	String fileId = fileService.save(fileList.get(i),path,Module.ACTIVITY.getCode(), title, null,request);
            	schoolmapper.updateschoolUpload(upid, fileId);
        	}
        }
        record.setDeleted(false);
		record.setUpdatedTime(new Date());
		record.setFabu(false);
		record.setFabuState(0);
		record.setId(upid);
		int num=schoolmapper.updateByPrimaryKeySelective(record);
		return new BaseResult<T>(true, num, "修改成功");
	}
}
