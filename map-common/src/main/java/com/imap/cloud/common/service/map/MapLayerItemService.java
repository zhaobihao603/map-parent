package com.imap.cloud.common.service.map;

import java.util.List;

import com.imap.cloud.common.entity.map.MapLayer;
import com.imap.cloud.common.entity.map.MapLayerItem;
import com.imap.cloud.common.entity.system.DictionaryItem;
import com.imap.cloud.common.service.base.BaseService;

/**
 * 
 * @author Hxin
 *
 */
public interface MapLayerItemService extends BaseService<MapLayerItem,String>{
	
	public List<MapLayerItem> listByLayerId(String layerId) throws Exception;

	public List<MapLayerItem> listByRange(String areaId, MapLayer layer,
			String name, Double startX, Double startY, Double endX, Double endY);

	public List<MapLayerItem> listByArea(MapLayer mapLayer, String areaId, String name);

}
