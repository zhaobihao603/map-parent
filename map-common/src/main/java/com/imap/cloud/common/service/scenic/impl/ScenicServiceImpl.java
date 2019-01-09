package com.imap.cloud.common.service.scenic.impl;

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
import com.imap.cloud.common.dao.scenic.ScenicMapper;
import com.imap.cloud.common.dto.BaseResult;
import com.imap.cloud.common.entity.scenic.Scenic;
import com.imap.cloud.common.enums.Module;
import com.imap.cloud.common.service.base.impl.BaseServiceImpl;
import com.imap.cloud.common.service.scenic.ScenicService;
import com.imap.cloud.common.service.system.FileService;
import com.imap.cloud.common.util.AccountUtils;

@LogDescription(name="景点服务")
@Service
public class ScenicServiceImpl extends BaseServiceImpl<Scenic, String> implements ScenicService {

	@Autowired
	private ScenicMapper scenicMapper;
	@Autowired
	private FileService fileService;
	
	@Autowired
	ScenicServiceImpl(ScenicMapper scenicMapper){
		this.scenicMapper =  scenicMapper;
		super.setBaseDao(scenicMapper);
	}
	
	@Override
	@LogDescription(name="景点删除记录")
	@Transactional
	public int deleteByPrimaryKey(String id) {
		return scenicMapper.deleteByPrimaryKey(id);
	}

	@Override
	public Scenic selectByPrimaryKey(String id) {
		return scenicMapper.selectByPrimaryKey(id);
	}

	@Override
	@LogDescription(name="景点插入记录")
	@Transactional
	public int inserts(Scenic scenic) {
		scenic.setId(AccountUtils.getUUID());
		scenic.setCreatetime(new Date());
		return scenicMapper.insert(scenic);
	}

	@Override
	@LogDescription(name="景点更改记录")
	@Transactional
	public int updateByPks(Scenic scenic) {
		try {
			return scenicMapper.updateByPks(scenic);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	@Override
	@LogDescription(name="景点删除记录")
	@Transactional
	public int deleteUploadId(String uploadId) {
		return scenicMapper.deleteUploadId(uploadId);
	}

	@Override
	@LogDescription(name="景点更改默认图片记录")
	@Transactional
	public int updatePhotoId(String default_photo_id, String id) {
		return scenicMapper.updatePhotoId(default_photo_id, id);
	}
	
	@Override
	@LogDescription(name="新增景点图片记录")
	@Transactional
	public int uploadScenicPic(String id,String title,String desc,HttpServletRequest request) {
		CommonsMultipartResolver commonsMultipartResolver = new   
        		CommonsMultipartResolver(request.getSession().getServletContext());  
        //设置编码  
        commonsMultipartResolver.setDefaultEncoding("utf-8"); 
        int num=0;
        //判断 request 是否有文件上传,即多部分请求...  
        if (commonsMultipartResolver.isMultipart(request)) { 
        	//转换成多部分request  
        	MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request; 
        	List<MultipartFile> fileList = multipartRequest.getFiles("files");
        	for(MultipartFile file:fileList){
        		String path = fileService.uploadFile(file, Module.SCENIC.getCode(), request);
            	String fileId = fileService.save(file,path,Module.SCENIC.getCode(), title, desc,request);
            	num += scenicMapper.linkScenicUpload(id, fileId);
        	}
        }
		return num;
	}

	/**
	 *  1、如果有文件上传  删除原有的关联 ，建立新的上传文件关联    
	 *  2、没有文件上传，更改其它信息 原关联表数据不变
	 */
	@Override
	@LogDescription(name="景点更改图片记录")
	@Transactional
	public BaseResult<T> updatePhoto(String id,String uploadId,String title,String desc,HttpServletRequest request){
		//创建一个通用的多部分解析器.  
        CommonsMultipartResolver cmr = new CommonsMultipartResolver(request.getSession().getServletContext());  
        //设置编码  
        cmr.setDefaultEncoding("utf-8");  
        boolean flag = false;//是否更新标志
        if (cmr.isMultipart(request)) {  
        	MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;    
            List<MultipartFile> fileList = multipartRequest.getFiles("files");
            
            if(fileList!=null && fileList.size()>0){
            	String path = fileService.uploadFile(fileList.get(0), Module.SCENIC.getCode(), request);
            	String fileId = fileService.save(fileList.get(0),path,Module.SCENIC.getCode(), title, desc,request);
            	scenicMapper.deleteUploadId(uploadId);//删除旧图片的关联记录
            	scenicMapper.linkScenicUpload(id, fileId);//关联新图片
            	flag = true;
            }
        }
        if(!flag){//当没有上传文件，只更新title和描述
        	//定向更新
        	fileService.update(uploadId,null, Module.SCENIC.getCode(), title, desc,null);
        }
        return new BaseResult<T>(true, 1, "修改成功");
	}
}
