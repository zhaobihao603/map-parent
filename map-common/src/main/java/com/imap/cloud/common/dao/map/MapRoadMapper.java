/*
 * MapRoadMapper.java
 * Copyright(C) 20xx-2015 xxxxxx��˾
 * All rights reserved.
 * -----------------------------------------------
 * 2017-04-21 Created
 */
package com.imap.cloud.common.dao.map;

import java.util.List;

import com.imap.cloud.common.entity.map.MapRoad;

public interface MapRoadMapper {
    int deleteByPrimaryKey(String id);

    int insert(MapRoad record);

    MapRoad selectByPrimaryKey(String id);

    int updateById(MapRoad record);

	int deleteByArea(String areaId);

	List<MapRoad> getList(MapRoad entity);
}