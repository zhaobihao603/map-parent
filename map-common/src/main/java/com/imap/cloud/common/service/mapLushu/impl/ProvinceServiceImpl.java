package com.imap.cloud.common.service.mapLushu.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imap.cloud.common.aop.annotation.LogDescription;
import com.imap.cloud.common.dao.mapLushu.ProvinceMapper;
import com.imap.cloud.common.entity.mapLushu.Province;
import com.imap.cloud.common.service.base.impl.BaseServiceImpl;
import com.imap.cloud.common.service.mapLushu.ProvinceService;

@LogDescription(name="读取shpfile")
@Service
public class ProvinceServiceImpl extends BaseServiceImpl< Province, String> implements ProvinceService {

	@Autowired ProvinceMapper mapper;
	@Override
	@Transactional
	public int insertItem(Province entity) {
		//int num = mapper.insertFeature(entity);
		return 0;
	}
	@Override
	@Transactional
	public int insertFeature(Province entity) {
		int num = mapper.insertFeature(entity);
		return num;
	}
	@Override
	@Transactional
	public int updateFeature(Province province) {
		int num= mapper.updateFeature(province);
		return num;
	}
	@Override
	@Transactional
	public int deleteFeature(int fid) {
		int num =mapper.deleteFeature(fid);
		return num;
	}
	@Override
	@Transactional
	public int saveShpMsg(List<Province> list) {
		int num = mapper.deleteTb();
		if(num>=0){
			for (Province province : list) {
				mapper.insert(province);
			}
			
		}
		return 0;
	}

}
