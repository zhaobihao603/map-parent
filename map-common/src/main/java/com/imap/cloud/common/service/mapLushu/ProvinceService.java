package com.imap.cloud.common.service.mapLushu;

import java.util.List;

import com.imap.cloud.common.entity.mapLushu.Province;
import com.imap.cloud.common.service.base.BaseService;

public interface ProvinceService extends BaseService<Province,String> {
	
	int insertItem(Province entity);
	
	int insertFeature(Province entity);
	
	int updateFeature(Province province);
	
	int deleteFeature(int fid);
	
	//导入shapefile
	int saveShpMsg(List<Province> list);
}
