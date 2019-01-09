package com.imap.cloud.common.service.system.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.imap.cloud.common.aop.annotation.LogDescription;
import com.imap.cloud.common.dao.system.DictionaryMapper;
import com.imap.cloud.common.dao.system.UnitLabelMapper;
import com.imap.cloud.common.dto.Pager;
import com.imap.cloud.common.dto.UnitDTO;
import com.imap.cloud.common.entity.system.SysFile;
import com.imap.cloud.common.entity.system.UnitLabel;
import com.imap.cloud.common.enums.Module;
import com.imap.cloud.common.service.system.FileService;
import com.imap.cloud.common.service.system.UnitService;
import com.imap.cloud.common.util.AccountUtils;

/**
 * 单位及单位类型服务层实现类
 * @author Bge
 * @since 2016-11-02
 */
@LogDescription(name="单位及单位类型的服务")
@Service
public class UnitServiceImpl implements UnitService {
	
	@Autowired
	private UnitLabelMapper unitLabelMapper;

	@Autowired
	private DictionaryMapper dictionaryMapper;
	
	@Autowired
	private FileService fileService;
	
	@Override
	public List<UnitLabel> getUnitLabels() {
		return unitLabelMapper.selectUnitLabels();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Pager<UnitLabel> pageUnitLabelsByConditions(Integer pageNum,Integer pageSize,Map conditionMap) {
		//PageHelper只对紧跟着的第一个SQL语句起作用
		Page<UnitLabel> p = PageHelper.startPage(pageNum==null?1:pageNum,pageSize==null?10:pageSize,true);
		//pageList 其实是一个 包含结果的com.github.pagehelper.Page
		// pageList instanceof com.github.pagehelper.Page == true
		List<UnitLabel> unitLst = unitLabelMapper.selectUnitLabelsByConditions(conditionMap);
		Pager<UnitLabel> pageUnitLst = new Pager(unitLst,p);
		return pageUnitLst;
	}
	
	@Override
	public List<UnitLabel> listUnitLabelsByConditions(Map conditionMap) {
		return  unitLabelMapper.selectUnitLabelsByConditions(conditionMap);
	}
	
	@Override
	public UnitLabel getUnitLabel(String id) {
		return unitLabelMapper.selectByPk(id);
	}

	@Override
	@LogDescription(name="单位修改")
	@Transactional
	public void update(UnitDTO unitDTO) {
		unitLabelMapper.updateByPkSelective(unitDTO);
	}

	@Override
	@LogDescription(name="单位保存")
	@Transactional
	public void save(UnitDTO unitDTO) {
		unitDTO.setId(AccountUtils.getUUID());
		unitLabelMapper.insert(unitDTO);
	}

	@Override
	@LogDescription(name="单位删除")
	@Transactional
	public void removeUnitLabel(String id) {
		//删除单位和图片中间表中对应的记录
		unitLabelMapper.deleteUnitImages(id);
		//删除单位表中对应的单位信息
		unitLabelMapper.deleteUnitLabelById1(id);
	}
	
	@Override
	@LogDescription(name="单位图片保存")
	@Transactional
	public void saveImage(String labelId, String imageId) {
		unitLabelMapper.insertImage(labelId,imageId);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Map getImagesByLabelId(String labelId) {
		Map resultMap = new HashMap();
		UnitLabel unitLabel = unitLabelMapper.selectByPk(labelId);
		List<String> list = unitLabelMapper.selectImagesIdByLabelId(labelId);
		String[] lst = new String[list.size()];
		for (int i = 0; i < lst.length; i++) {
			lst[i] = list.get(i);
		}
		List<SysFile> lstImages = null;
		if(lst.length!=0){
			lstImages = fileService.selectByIds(lst);
			resultMap.put("images", lstImages);
		}else{
			resultMap.put("images", new ArrayList<SysFile>());
		}
		resultMap.put("defaultImageId", unitLabel.getImageId());
		return resultMap;
	}

	@Override
	@LogDescription(name="单位图片删除")
	@Transactional
	public void delImage(String labelId, String imageId) {
		//查询单位信息
		UnitLabel unit = unitLabelMapper.selectByPk(labelId);
		//判断删除的图片是否为默认图片
		if(imageId.equals(unit.getImageId())){
			unitLabelMapper.updateImageId(labelId, null);
		}
		unitLabelMapper.deleteUnitImage(labelId,imageId);
	}
	
	@Override
	@LogDescription(name="设置默认单位图片")
	@Transactional
	public void setDefaultImage(String labelId,String imageId) {
		unitLabelMapper.updateImageId(labelId,imageId);
	}
	
	@Override
	@LogDescription(name="根据关键字搜索单位")
	@Transactional
	public Pager<UnitLabel> search(String keyWord, Integer pageSize,Integer currentPage){
		Map<String,Object> conditionMap = new<String,Object> HashMap();
		//conditionMap.put("mainTypeId",41);
		//conditionMap.put("typeId",48);
		//conditionMap.put("name",keyWord);
		//conditionMap.put("address",keyWord);
		conditionMap.put("keyword", keyWord);
		Page<UnitLabel> p = PageHelper.startPage(currentPage==null?1:currentPage,pageSize==null?10:pageSize,true);
		List<UnitLabel> unitLst = unitLabelMapper.searchUnitLabels(conditionMap);
		return new Pager(unitLst);
	}
	
	@LogDescription(name="更新默认图片或者添加单位图片")
	@Transactional
	public void addImage(String labelId, String oldImageId, String title,
			String description,HttpServletRequest request){
		//创建一个通用的多部分解析器.  
		CommonsMultipartResolver commonsMultipartResolver = new   
				CommonsMultipartResolver(request.getSession().getServletContext());  
		//设置编码  
		commonsMultipartResolver.setDefaultEncoding("utf-8");  
		//判断 request 是否有文件上传,即多部分请求...  
		if (commonsMultipartResolver.isMultipart(request)) {  
			//转换成多部分request  
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;    
			List<MultipartFile> fileList = multipartRequest.getFiles("files");
			
			String fileId = null;
			for(int i =0;fileList!=null && i<fileList.size();i++){
        		String path = fileService.uploadFile(fileList.get(i), Module.UNIT.getCode(), request);
            	fileId = fileService.save(fileList.get(i),path,Module.UNIT.getCode(), title, null,request);
            	unitLabelMapper.insertImage(labelId,fileId);//单位图片，中间表中插入一条数据
        	}
			if(oldImageId!=null && fileId!=null){
				unitLabelMapper.deleteUnitImage(labelId,oldImageId);//删除中间表中oldImageId相应的记录
				unitLabelMapper.updateImageId(labelId, fileId);//更新默认图片
			}
		}
	}

	@Override
	public List<Map> selectSecByParentIds(List list) {
		List<Map> rtList= unitLabelMapper.selectSecByParentIds(list);
		return rtList;
	}
	
}
