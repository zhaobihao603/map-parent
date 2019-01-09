package com.imap.cloud.common.service.map;

import java.util.List;
import java.util.Map;

import com.imap.cloud.common.entity.map.MapLayerRender;


/**
 * @author Administrator
 *
 */
public interface MapLayerRenderService{
	
	/**
	 * @param layerId
	 * @param showId
	 * @param valueFields 
	 * @return
	 */
	public MapLayerRender selectByPk(String layerId,String showId, String valueFields);
	
	/**
	 * @param mapLayerRender
	 * @return
	 */
	public Integer insert(MapLayerRender mapLayerRender);
	
	/**
	 * @param mapLayerRender
	 * @return
	 */
	public Integer updateByPkSelective(MapLayerRender mapLayerRender);

	/**
	 * 
	 * @param layerId
	 * @param showId
	 * @return
	 */
	public Integer deleteByPk(String layerId,String showId);

	/**
	 * 获取map_layer_item表字段及类型
	 * @param usedDatabaseName 
	 * @param columnsType 
	 * @return
	 */
	public List<String> getCusFeatureType(String usedDatabaseName, List<String> columnsType);

	/**
	 * @param layerId
	 * @param valueFields
	 * @return 
	 */
	public List<Map<String, String>> selectByValueFields(String layerId, String[] valueFields);

	/**
	 * @param layerId
	 * @param valueFields
	 * @return
	 */
	public List<Map<String, String>> selectByDBValue(String layerId,Integer classes,
			String[] valueFields);
}
