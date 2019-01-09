package com.imap.cloud.common.service.map.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imap.cloud.common.dao.map.MapLayerItemMapper;
import com.imap.cloud.common.entity.map.MapLayer;
import com.imap.cloud.common.entity.map.MapLayerItem;
import com.imap.cloud.common.service.base.impl.BaseServiceImpl;
import com.imap.cloud.common.service.map.MapLayerItemService;

@Service
public class MapLayerItemServiceImpl extends BaseServiceImpl<MapLayerItem,String> implements MapLayerItemService {
	@Autowired
	private MapLayerItemMapper mapLayerItemMapper;
	
	//这句必须要加上。不然会报空指针异常，因为在实际掉用的时候不是BaseDao调用，而是这个mapDirMapper
	@Autowired
	public void setBaseMapper(){
		super.setBaseDao(mapLayerItemMapper);
	}

	@Override
	public List<MapLayerItem> listByLayerId(String layerId) throws Exception {
		return mapLayerItemMapper.listByLayerId(layerId);
	}
	
	public List<MapLayerItem> listByRange(String areaId, MapLayer layer,
			String name, Double startX, Double startY, Double endX, Double endY){
		
		return mapLayerItemMapper.listByRange(areaId, layer,
				name, startX, startY, endX, endY);
	}

	@Override
	public List<MapLayerItem> listByArea(MapLayer mapLayer, String areaId, String name) {
		return mapLayerItemMapper.listByArea(mapLayer,areaId,name);
	}

}
