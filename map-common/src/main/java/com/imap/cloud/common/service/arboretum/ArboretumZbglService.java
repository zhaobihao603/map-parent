package com.imap.cloud.common.service.arboretum;

import java.util.List;

import com.imap.cloud.common.entity.arboretum.ArboretumZbgl;
import com.imap.cloud.common.service.base.BaseService;

/**
 * 植被管理service
 * @author 冯林
 *
 */
public interface ArboretumZbglService extends BaseService<ArboretumZbgl, String> {
	
	/**
	 * 删除负责人
	 * 和树木的信息(中间表)
	 * @param id
	 * @throws Exception 
	 */
	void deleteByPkShu(String id) throws Exception;
	
	/**
	 * 添加植被管理
	 * 同时添加该植被管理区域下的树木信息(中间表)
	 * @param shu
	 * @throws Exception 
	 */
	void insertPkShuId(ArboretumZbgl shu) throws Exception;
	
	/**
	 * 修改植被管理
	 * 同时修改该植被管理区域下的树木信息(中间表)
	 * @param shu
	 */
	void updateByPkShuId(ArboretumZbgl shu)throws Exception;
	
	/**
	 *  返回该校区的所有植被管理信息
	 * @param area 校区
	 * @return 
	 */
	List<ArboretumZbgl> selectFindAll(String area);
}
