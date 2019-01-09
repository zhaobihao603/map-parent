package com.imap.cloud.common.service.map;

import com.imap.cloud.common.entity.map.MapNote;
import com.imap.cloud.common.service.base.BaseService;

/**
 * 
 * @author 
 *
 */
public interface MapNoteService extends BaseService<MapNote,String>{
	
	int deleteByPrimaryKey(String id);

    int insertSelective(MapNote record);

    MapNote selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MapNote record);

    int updateByPrimaryKey(MapNote record);
}
