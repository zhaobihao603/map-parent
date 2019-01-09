/*
 * MapLayerMapper.java
 * Copyright(C) 20xx-2015 
 * All rights reserved.
 * -----------------------------------------------
 * 2016-10-18 Created
 */
package com.imap.cloud.common.dao.map;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.imap.cloud.common.entity.map.MapLayerRender;


public interface MapLayerRenderMapper{
	/**
	 * @param layerId
	 * @param showId
	 * @param valueFields 
	 * @return
	 */
	public MapLayerRender selectByPk(@Param("layerId")String layerId,
			@Param("showId")String showId,@Param("valueFields")String valueFields);
	
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
	public Integer deleteByPk(@Param("layerId")String layerId,@Param("showId")String showId);

	/**
	 * 从标注表查询指定数据类型的字段
	 * @param columnsType 
	 * @return
	 */
	public List<String> selectColumns(@Param("usedDatabaseName")String usedDatabaseName,@Param("columnsType")List<String> columnsType);

	/**
	 * 根据属性分组查询值
	 * @param layerId
	 * @param valueFields
	 * @return
	 */
	public List<Map<String, String>> selectByValueFields(@Param("layerId")String layerId,
			@Param("valueFields")String[] valueFields);
}



