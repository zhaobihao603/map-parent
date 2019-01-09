package com.imap.cloud.common.dao.arboretum;

import java.util.List;
import java.util.Map;

import com.imap.cloud.common.dao.base.BaseDao;
import com.imap.cloud.common.entity.arboretum.ArboretumZbgl;

/**
 * 植被管理DAO
 * @author 冯林
 *
 */
public interface ArboretumZbglMapper extends BaseDao<ArboretumZbgl, String>{
	
	/**
	 * 删除负责人
	 * 和树木的信息(中间表)
	 * @param id
	 */
	void deleteByPkShu(String id);

	/**
	 * 添加植被信息和树木的信息
	 * @param map
	 */
	void insertPkShuId(Map<String, Object> map);
	
	/**
	 *  返回该校区的所有植被管理信息
	 * @param area 校区
	 * @return 
	 */
	List<ArboretumZbgl> selectFindAll(String area);

}
