package com.imap.cloud.common.service.teachers.impl;

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
import com.imap.cloud.common.dao.teachers.TeachersMapper;
import com.imap.cloud.common.dto.BaseResult;
import com.imap.cloud.common.entity.teachers.Teachers;
import com.imap.cloud.common.enums.Module;
import com.imap.cloud.common.service.base.impl.BaseServiceImpl;
import com.imap.cloud.common.service.system.FileService;
import com.imap.cloud.common.service.teachers.TeachersService;
import com.imap.cloud.common.util.AccountUtils;
@LogDescription(name="师资力量")
@Service
public class TeachersServiceImpl extends BaseServiceImpl<Teachers, String> implements TeachersService {
	
	@Autowired
	private TeachersMapper teachersmapper;
	@Autowired
	private FileService fileService;
	
	@Autowired
	TeachersServiceImpl(TeachersMapper teachersmapper){
		this.teachersmapper =  teachersmapper;
		super.setBaseDao(teachersmapper);
	}

	@Override
	@LogDescription(name="师资力量删除")
	@Transactional
	public int deleteByPrimaryKey(String id) {
		return teachersmapper.deleteByPrimaryKey(id);
	}

	@Override
	public Teachers selectByPrimaryKey(String id) {
		return teachersmapper.selectByPrimaryKey(id);
	}

	@Override
	@LogDescription(name="师资力量中间表关联")
	@Transactional
	public int linkTeachersUpload(String teachersId, String uploadId) {
		return teachersmapper.linkTeachersUpload(teachersId, uploadId);
	}

	@Override
	@LogDescription(name="师资力量删除中间表关联")
	@Transactional
	public int deleteTeachersUpload(String teachersId) {
		return teachersmapper.deleteTeachersUpload(teachersId);
	}
	
	@Override
	public List<Teachers> selectLaboratory(String area) {
		return teachersmapper.selectLaboratory(area);
	}

	@Override
	@LogDescription(name="师资力量插入")
	@Transactional
	public int insert(Teachers teachers, String title, HttpServletRequest request){
		CommonsMultipartResolver commonsMultipartResolver = new   
        		CommonsMultipartResolver(request.getSession().getServletContext());  
        //设置编码  
        commonsMultipartResolver.setDefaultEncoding("utf-8"); 
        teachers.setId(AccountUtils.getUUID());
        teachers.setDeleted("0");
		teachers.setCreateTime(new Date());
		teachers.setUpdateTime(new Date());
        int num= teachersmapper.insert(teachers);
        //判断 request 是否有文件上传,即多部分请求...  
        if (commonsMultipartResolver.isMultipart(request)) { 
        	//转换成多部分request  
        	MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request; 
        	List<MultipartFile> fileList = multipartRequest.getFiles("files");
        	for(MultipartFile file:fileList){
        		String path = fileService.uploadFile(file, Module.TEACHER.getCode(), request);
            	String fileId = fileService.save(file,path,Module.TEACHER.getCode(), title, null,request);
            	teachersmapper.linkTeachersUpload(teachers.getId(), fileId);
        	}
        }
        return num;
	}

	@Override
	@LogDescription(name="师资力量修改")
	@Transactional
	public BaseResult<T> update(Teachers teachers, String id, String title,
			HttpServletRequest request) {
		CommonsMultipartResolver commonsMultipartResolver = 
				new CommonsMultipartResolver(request.getSession().getServletContext());
		commonsMultipartResolver.setDefaultEncoding("utf-8");
		int num = 0;
		if (commonsMultipartResolver.isMultipart(request)) {
			// 转换成多部分request
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			List<MultipartFile> fileList = multipartRequest.getFiles("files");
			for (MultipartFile file : fileList) {
				String path = fileService.uploadFile(file, Module.TEACHER.getCode(),
						request);
				String fileId = fileService.save(file, path, Module.TEACHER.getCode(),
						title, null, request);
				teachersmapper.linkTeachersUpload(id, fileId);
			}
			teachers.setDeleted("0");
			teachers.setUpdateTime(new Date());
			teachers.setId(id);
			num = teachersmapper.updateByPrimaryKeySelective(teachers);
			return new BaseResult<T>(true, num, "成功");
		}else{
			teachers.setDeleted("0");
			teachers.setUpdateTime(new Date());
			teachers.setId(id);
			num = teachersmapper.updateByPrimaryKeySelective(teachers);
			return new BaseResult<T>(true, num, "成功");
		}
	}

}
