package com.imap.cloud.common.dao.map;

import com.imap.cloud.common.dao.base.BaseDao;
import com.imap.cloud.common.entity.map.MapNote;
import org.springframework.stereotype.Repository;


@Repository
public interface MapNoteMapper extends BaseDao<MapNote, String>{
	
	 	int deleteByPrimaryKey(String id);

	    int insert(MapNote record);

	    int insertSelective(MapNote record);

	    MapNote selectByPrimaryKey(String id);

	    int updateByPrimaryKeySelective(MapNote record);

	    int updateByPrimaryKey(MapNote record);
}