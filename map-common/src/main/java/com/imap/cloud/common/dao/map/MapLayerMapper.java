/*
 * MapLayerMapper.java
 * Copyright(C) 20xx-2015 
 * All rights reserved.
 * -----------------------------------------------
 * 2016-10-18 Created
 */
package com.imap.cloud.common.dao.map;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.imap.cloud.common.dao.base.BaseDao;
import com.imap.cloud.common.entity.map.MapLayer;


public interface MapLayerMapper extends BaseDao<MapLayer, String> {

	List<MapLayer> listByDirId(@Param("dirId")String dirId);

	List<MapLayer> getBy(MapLayer entity);

}