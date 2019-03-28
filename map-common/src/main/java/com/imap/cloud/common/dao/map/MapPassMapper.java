/*
 * MapPassMapper.java
 * Copyright(C) 20xx-2015 xxxxxx��˾
 * All rights reserved.
 * -----------------------------------------------
 * 2017-04-21 Created
 */
package com.imap.cloud.common.dao.map;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.imap.cloud.common.entity.map.MapPass;
import org.springframework.stereotype.Repository;

@Repository
public interface MapPassMapper {
    int deleteBy(@Param("start")String start,@Param("end")String end);
    
    void deleteById(@Param("id")String id);

    int insert(MapPass record);

    MapPass selectByPrimaryKey(@Param("id")String id);
    
    MapPass getByStartEnd(@Param("start")String start,@Param("end")String end);

    int updateByPk(MapPass record);

	int deleteByArea(String areaId);

	List<MapPass> getByAreaId(String areaId);
	
	List<MapPass> getAvailableByAreaId(@Param("type")String type,@Param("areaId")String areaId);

	List<MapPass> getList(MapPass entity);

	/**
	 * 根据两端点获取双向车道
	 * @param start
	 * @param end
	 * @return
	 */
	List<MapPass> get2Way(@Param("p1")String start, @Param("p2")String end);

}