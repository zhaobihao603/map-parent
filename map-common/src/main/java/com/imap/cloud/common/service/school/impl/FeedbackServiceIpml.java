package com.imap.cloud.common.service.school.impl;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.imap.cloud.common.aop.annotation.LogDescription;
import com.imap.cloud.common.dao.school.FeedbackMapper;
import com.imap.cloud.common.entity.school.Feedback;
import com.imap.cloud.common.enums.Module;
import com.imap.cloud.common.service.base.impl.BaseServiceImpl;
import com.imap.cloud.common.service.school.FeedbackService;
import com.imap.cloud.common.service.system.FileService;
import com.imap.cloud.common.util.AccountUtils;

@LogDescription(name="反馈服务")
@Service("feedbackServiceIpml")
public class FeedbackServiceIpml extends BaseServiceImpl<Feedback, String> implements FeedbackService{
	
	private FeedbackMapper feedbackMapper;
	@Autowired private FileService fileService;
	
	@Autowired
	FeedbackServiceIpml(FeedbackMapper feedbackMapper){
		super.setBaseDao(feedbackMapper);
		this.feedbackMapper = feedbackMapper;
	}
	
	@Transactional
	public int save(Feedback feedback, HttpServletRequest request) throws Exception{
		//创建一个通用的多部分解析器.  
        CommonsMultipartResolver cmr = new CommonsMultipartResolver(request.getSession().getServletContext());  
        //设置编码  
        cmr.setDefaultEncoding("utf-8");
        int i = 0;
        if (cmr.isMultipart(request)) {  
        	MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;    
            List<MultipartFile> fileList = multipartRequest.getFiles("files");
            if(fileList!=null && fileList.size()>0){
            	String path = fileService.uploadFile(fileList.get(0), Module.FEEDBACK.getCode(), request);
            	String fileId = fileService.save(fileList.get(0),path,Module.FEEDBACK.getCode(), feedback.getTitle(), null,request);
            	feedback.setImageUrl(path);
            }
        }
        if(!StringUtils.isNotBlank(feedback.getId())){
			feedback.setId(AccountUtils.getUUID());
			feedback.setCreateDate(AccountUtils.format.format(new Date()));
			i = feedbackMapper.insertSelective(feedback);
		}else{
			i = feedbackMapper.updateByPk(feedback);
		}
        return i;
	}
}
