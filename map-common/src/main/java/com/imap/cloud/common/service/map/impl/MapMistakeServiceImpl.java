package com.imap.cloud.common.service.map.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.imap.cloud.common.dao.map.MapMistakeMapper;
import com.imap.cloud.common.entity.map.MapMistake;
import com.imap.cloud.common.enums.Module;
import com.imap.cloud.common.service.base.impl.BaseServiceImpl;
import com.imap.cloud.common.service.map.MapMistakeService;
import com.imap.cloud.common.service.system.FileService;
import com.imap.cloud.common.util.AccountUtils;

@Service("mapMistakeServiceImpl")
public class MapMistakeServiceImpl extends BaseServiceImpl<MapMistake,String> implements MapMistakeService {
	
	private MapMistakeMapper mapMistakeMapper;
	
	@Autowired private FileService fileService;
	
	@Autowired
	MapMistakeServiceImpl(MapMistakeMapper mapMistakeMapper){
		super.setBaseDao(mapMistakeMapper);
		this.mapMistakeMapper = mapMistakeMapper;
	}

	public List<MapMistake> selectFindAll() {
		return mapMistakeMapper.selectFindAll();
	}
	
	@Transactional
	public int save(MapMistake mistake, HttpServletRequest request) throws Exception{
		//创建一个通用的多部分解析器.  
        CommonsMultipartResolver cmr = new CommonsMultipartResolver(request.getSession().getServletContext());  
        //设置编码  
        cmr.setDefaultEncoding("utf-8");
        int i = 0;
        if (cmr.isMultipart(request)) {  
        	MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;    
            List<MultipartFile> fileList = multipartRequest.getFiles("files");
            if(fileList!=null && fileList.size()>0){
            	String path = fileService.uploadFile(fileList.get(0), Module.MISTAKE.getCode(), request);
            	String fileId = fileService.save(fileList.get(0),path,Module.MISTAKE.getCode(), null, mistake.getContent(),request);
            	mistake.setImageUrl(path);
            }
        }
        if(!StringUtils.isNotBlank(mistake.getId())){
        	mistake.setId(AccountUtils.getUUID());
			i = mapMistakeMapper.insertSelective(mistake);
		}else{
			i = mapMistakeMapper.updateByPkSelective(mistake);
		}		
		return i;
	}
	
}
