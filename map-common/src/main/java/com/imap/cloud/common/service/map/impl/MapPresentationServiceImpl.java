package com.imap.cloud.common.service.map.impl;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.alibaba.druid.util.StringUtils;
import com.github.pagehelper.PageHelper;
import com.imap.cloud.common.aop.annotation.LogDescription;
import com.imap.cloud.common.dao.map.MapPresentationMapper;
import com.imap.cloud.common.entity.map.MapPresentation;
import com.imap.cloud.common.entity.map.MapPresentationExample;
import com.imap.cloud.common.enums.Module;
import com.imap.cloud.common.service.map.MapPresentationService;
import com.imap.cloud.common.service.system.FileService;
import com.imap.cloud.common.util.AccountUtils;

@LogDescription(name="解说服务")
@Transactional
@Service
public class MapPresentationServiceImpl implements MapPresentationService {
	@Autowired
	private MapPresentationMapper mapper;
	@Autowired
	private FileService fileService;
	private final String projection = "EPSG:32651";
	
	@Override
	public long countByExample(MapPresentationExample example) {
		try {
			//Method countByExample = mapper.getClass().getDeclaredMethod("countByExample", example.getClass());
			//Object result = countByExample.invoke(mapper, example);
			long result = mapper.countByExample(example);
			return Integer.parseInt(String.valueOf(result));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	@Override
	public int deleteByExample(MapPresentationExample example) {
		try {
			return mapper.deleteByExample(example);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	@Override
	public int deleteByPrimaryKey(String id) {
		try {
			return mapper.deleteByPrimaryKey(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	@Override
	public int insert(MapPresentation record) {
		try {
			return mapper.insert(record);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	@Override
	public int insertSelective(MapPresentation record) {
		try {
			return mapper.insertSelective(record);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	@Override
	public List<MapPresentation> selectByExample(MapPresentationExample example) {
		try {
			return mapper.selectByExample(example);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public List<MapPresentation> selectByExampleForStartPage(
			MapPresentationExample example, Integer pageNum, Integer pageSize) {
		try {
			PageHelper.startPage(pageNum, pageSize, false);
			List<MapPresentation> pageList = mapper.selectByExample(example);
			return pageList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public List<MapPresentation> selectByExampleForOffsetPage(
			MapPresentationExample example, Integer offset, Integer limit) {
		try {
			PageHelper.offsetPage(offset, limit, false);
			List<MapPresentation> pageList = mapper.selectByExample(example);
			return pageList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public MapPresentation selectFirstByExample(MapPresentationExample example) {
		try {
			List<MapPresentation> result = (List<MapPresentation>) mapper.selectByExample(example);
			if (null != result && result.size() > 0) {
				return result.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public MapPresentation selectByPrimaryKey(String id) {
		try {
			return mapper.selectByPrimaryKey(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public int updateByExampleSelective(
			@Param("record") MapPresentation record,
			@Param("example") MapPresentationExample example) {
		try {
			Object result = mapper.updateByExampleSelective(record, example);
			return Integer.parseInt(String.valueOf(result));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	@Override
	public int updateByExample(@Param("record") MapPresentation record,
			@Param("example") MapPresentationExample example) {
		try {
			return mapper.updateByExample(record, example);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	@Override
	public int updateByPrimaryKeySelective(MapPresentation record) {
		try {
			return mapper.updateByPrimaryKeySelective(record);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	@Override
	public int updateByPrimaryKey(MapPresentation record) {
		try {
			return mapper.updateByPrimaryKey(record);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	@Override
	public int deleteByPrimaryKeys(String ids) {
		try {
			if (StringUtils.isEmpty(ids)) {
				return 0;
			}
			String[] idArray = ids.split("-");
			int count = 0;
			for (String idStr : idArray) {
				if (StringUtils.isEmpty(idStr)) {
					continue;
				}
				count += mapper.deleteByPrimaryKey(idStr);
			}
			return count;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public List<Map<String, Object>> searchAround(double distance, double x, double y){
		try {
			int times = 1;
			if("EPSG:4490".equals(projection)||"EPSG:4326".equals(projection)){
				times *= 100000; 
			}
			return mapper.searchAround(distance/times, x, y,times);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Map<String, Object> getNearest(double x, double y){
		try {
			return mapper.getNearest( x, y);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Map<String, Object> getById(String id){
		try {
			return mapper.getById(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@LogDescription(name="解说记录保存")
	@Transactional
	public boolean saveWithFile(MapPresentation record,HttpServletRequest request){
		String vedioPath = null,imagePath = null;
		boolean rollback = false;
		try {
	        CommonsMultipartResolver cmr = new CommonsMultipartResolver(request.getSession().getServletContext());  
	        cmr.setDefaultEncoding("utf-8");  
	        if (cmr.isMultipart(request)) {
	        	MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
	        	MultipartFile video = multipartRequest.getFile("video");
	        	if(video!=null){
	        		vedioPath = fileService.uploadFile(video, Module.PRESENTATION.getCode(), request);
	        		String fileId = fileService.save(video,vedioPath,Module.PRESENTATION.getCode(), record.getTitle(), record.getSubhead(),request);
	        		record.setVideoId(fileId);
	        	}       	
	        	
	        	MultipartFile image = multipartRequest.getFile("image");
	        	if(image!=null){
	        		imagePath = fileService.uploadFile(image, Module.PRESENTATION.getCode(), request);
	        		String fileId = fileService.save(image,imagePath,Module.PRESENTATION.getCode(), record.getTitle(), record.getSubhead(),request);
	        		record.setImageId(fileId);
	        	}
	        }
	        if(StringUtils.isEmpty(record.getId())){
	        	record.setId(AccountUtils.getUUID());
				this.insertSelective(record);
			}else{
				this.updateByPrimaryKeySelective(record);
			}
	        return true;
		}catch (Exception e) {
			rollback = true;
			return false;
		}finally{
			if(rollback && vedioPath!=null){
				File f = new File(vedioPath);
				if(f.exists())f.delete();
			}
			if(rollback && imagePath!=null){
				File f = new File(imagePath);
				if(f.exists())f.delete();
			}				
		}
	}
	
	@Override
	public List<Map<String, Object>> getByExampleForStartPage(
			MapPresentationExample example, Integer pageNum, Integer pageSize) {
		try {
			int offset = (pageNum - 1) * pageSize;
			PageHelper.offsetPage(offset, pageSize, false);
			List<Map<String, Object>> pageList = mapper.getByExample(example);
			return pageList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
