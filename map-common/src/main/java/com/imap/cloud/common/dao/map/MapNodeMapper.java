/*
 * MapNodeMapper.java
 * Copyright(C) 20xx-2015 xxxxxx��˾
 * All rights reserved.
 * -----------------------------------------------
 * 2017-04-21 Created
 */
package com.imap.cloud.common.dao.map;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.imap.cloud.common.entity.map.MapNode;

public interface MapNodeMapper {
    int deleteByPrimaryKey(String id);

    int insert(MapNode record);

    MapNode selectByPrimaryKey(String id);

    int updateById(MapNode record);

	int deleteByArea(String areaId);

	MapNode getByXY(@Param("x")double x,@Param("y") double y);

	List<MapNode> getByAreaId(String areaId);
}