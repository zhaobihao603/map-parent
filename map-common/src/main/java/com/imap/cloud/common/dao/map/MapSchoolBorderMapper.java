package com.imap.cloud.common.dao.map;

import java.util.List;

import com.imap.cloud.common.entity.map.MapSchoolBorder;
import org.springframework.stereotype.Repository;

@Repository
public interface MapSchoolBorderMapper {
    int deleteByPrimaryKey(String id);

    int insert(MapSchoolBorder record);

    MapSchoolBorder selectByPrimaryKey(String id);

    int updateById(MapSchoolBorder record);
    
    int updateByArea(MapSchoolBorder record);

	int deleteByArea(String areaId);

	List<MapSchoolBorder> getByAreaId(String areaId);

	List<MapSchoolBorder> selectAll(MapSchoolBorder record);
}