package com.imap.cloud.common.dao.map;

import java.util.List;

import com.imap.cloud.common.dao.base.BaseDao;
import com.imap.cloud.common.entity.map.MapMistake;

public interface MapMistakeMapper extends BaseDao<MapMistake, String>{
	
	/**
	 * @return 查询所有的纠错信息
	 */
	List<MapMistake> selectFindAll();
}