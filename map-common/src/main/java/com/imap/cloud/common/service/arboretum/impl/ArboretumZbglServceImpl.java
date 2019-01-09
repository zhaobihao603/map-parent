package com.imap.cloud.common.service.arboretum.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imap.cloud.common.dao.arboretum.ArboretumZbglMapper;
import com.imap.cloud.common.entity.arboretum.ArboretumZbgl;
import com.imap.cloud.common.service.arboretum.ArboretumZbglService;
import com.imap.cloud.common.service.base.impl.BaseServiceImpl;

@Service("arboretumZbglServceIpml")
public class ArboretumZbglServceImpl extends BaseServiceImpl<ArboretumZbgl, String> implements ArboretumZbglService{
	
	private ArboretumZbglMapper arboretumZbglMapper;
	
	@Autowired
	ArboretumZbglServceImpl(ArboretumZbglMapper arboretumZbglMapper){
		this.arboretumZbglMapper = arboretumZbglMapper;
		super.setBaseDao(arboretumZbglMapper);
	}

	public void deleteByPkShu(String id) throws Exception {
		super.deleteByPk(id);
		arboretumZbglMapper.deleteByPkShu(id);
	}

	public void insertPkShuId(ArboretumZbgl shu) throws Exception {
		super.insert(shu);
		Map<String, Object> map = getShuIdStringArray(shu);
		if(map != null){
			arboretumZbglMapper.insertPkShuId(map);
		}
	}

	public void updateByPkShuId(ArboretumZbgl shu) throws Exception {
		super.updateByPk(shu);
		Map<String, Object> map = getShuIdStringArray(shu);
		if(map != null){
			arboretumZbglMapper.deleteByPkShu(shu.getId());
			arboretumZbglMapper.insertPkShuId(map);
		}
	}
	
	private Map<String,Object> getShuIdStringArray(ArboretumZbgl shu){
		if(StringUtils.isNotBlank(shu.getShuId())){
			String shuId = shu.getShuId();
			String[] split = shuId.split("-");
			Map<String,Object> map = new HashMap<>();
			map.put("zid", shu.getId());
			map.put("sid", split);
			return map;
		}
		return null;
	}

	public List<ArboretumZbgl> selectFindAll(String area) {
		return arboretumZbglMapper.selectFindAll(area);
	}
}
