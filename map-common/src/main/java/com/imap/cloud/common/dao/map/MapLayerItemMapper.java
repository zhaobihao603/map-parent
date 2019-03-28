package com.imap.cloud.common.dao.map;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.imap.cloud.common.dao.base.BaseDao;
import com.imap.cloud.common.entity.map.MapLayer;
import com.imap.cloud.common.entity.map.MapLayerItem;
import com.imap.cloud.common.entity.system.DictionaryItem;
import org.springframework.stereotype.Repository;

@Repository
public interface MapLayerItemMapper extends BaseDao<MapLayerItem, String>{
	List<MapLayerItem> listByLayerId(@Param("layerId")String layerId);

	List<MapLayerItem> listByRange(@Param("areaId")String areaId, @Param("layer")MapLayer layer, @Param("name")String name,
			@Param("startX")Double startX, @Param("startY")Double startY, 
			@Param("endX")Double endX, @Param("endY")Double endY);

	List<MapLayerItem> listByArea(@Param("layer")MapLayer mapLayer, @Param("areaId")String areaId, @Param("name")String name);
}