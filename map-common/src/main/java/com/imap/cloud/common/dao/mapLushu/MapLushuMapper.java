package com.imap.cloud.common.dao.mapLushu;

import java.util.List;

import com.imap.cloud.common.dao.base.BaseDao;
import com.imap.cloud.common.entity.mapLushu.MapLushu;

public interface MapLushuMapper extends BaseDao<MapLushu, String> {
	
	List<MapLushu> getBy(MapLushu entity);
	
	int insert(MapLushu entity);
	
	int deleteBylushuIdAndTime(MapLushu entity);
	
	MapLushu selectByid(String id);
}