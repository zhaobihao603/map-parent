package com.imap.cloud.common.dao.mapLushu;


import com.imap.cloud.common.dao.base.BaseDao;

import com.imap.cloud.common.entity.mapLushu.Province;
import org.springframework.stereotype.Repository;

@Repository
public interface ProvinceMapper extends BaseDao<Province, String> {
	
	int insert(Province province);
	
	public int insertFeature(Province province);
	
	int updateFeature(Province province);
	
	int deleteFeature(int id);
	
	//清空表数据
	public int deleteTb();
}