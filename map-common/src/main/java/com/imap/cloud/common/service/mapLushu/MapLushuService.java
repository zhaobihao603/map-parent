package com.imap.cloud.common.service.mapLushu;

import java.util.List;
import com.imap.cloud.common.entity.mapLushu.MapLushu;
import com.imap.cloud.common.service.base.BaseService;

public interface MapLushuService extends BaseService<MapLushu,String> {
	
	List<MapLushu> getBy(MapLushu entity);
	
	int insertItem(MapLushu entity);
	
	int deleteBylushuIdAndTime(MapLushu entity);
	
	MapLushu getMapLushuById(String id);
}
