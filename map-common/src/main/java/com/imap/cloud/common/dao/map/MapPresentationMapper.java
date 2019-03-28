package com.imap.cloud.common.dao.map;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.imap.cloud.common.entity.map.MapPresentation;
import com.imap.cloud.common.entity.map.MapPresentationExample;
import org.springframework.stereotype.Repository;

@Repository
public interface MapPresentationMapper {
    long countByExample(MapPresentationExample example);

    int deleteByExample(MapPresentationExample example);

    int deleteByPrimaryKey(String id);

    int insert(MapPresentation record);

    int insertSelective(MapPresentation record);

    List<MapPresentation> selectByExample(MapPresentationExample example);

    MapPresentation selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") MapPresentation record, @Param("example") MapPresentationExample example);

    int updateByExample(@Param("record") MapPresentation record, @Param("example") MapPresentationExample example);

    int updateByPrimaryKeySelective(MapPresentation record);

    int updateByPrimaryKey(MapPresentation record);

    List<Map<String, Object>> getByExample(MapPresentationExample example);

    List<Map<String, Object>> searchAround(
			@Param("distance") double distance,
			@Param("x") double x,
			@Param("y") double y,
			@Param("times") double times);
    
    Map<String, Object> getNearest(@Param("x") double x,@Param("y") double y);
    
    Map<String, Object> getById(String id);

}