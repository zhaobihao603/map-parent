package com.imap.cloud.common.service.map;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.imap.cloud.common.entity.map.MapMistake;
import com.imap.cloud.common.service.base.BaseService;


public interface MapMistakeService extends BaseService<MapMistake,String>{
	
	/**
	 * @return 查询所有的纠错信息
	 */
	List<MapMistake> selectFindAll();

	public int save(MapMistake mistake, HttpServletRequest request) throws Exception;
	
}
