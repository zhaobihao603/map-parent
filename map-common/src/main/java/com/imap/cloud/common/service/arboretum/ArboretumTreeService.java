package com.imap.cloud.common.service.arboretum;

import java.util.List;

import com.imap.cloud.common.entity.arboretum.ArboretumTree;
import com.imap.cloud.common.service.base.BaseService;

/**
 * 虚拟植物园  林学分类 service
 * @author 冯林
 *
 */
public interface ArboretumTreeService extends BaseService<ArboretumTree, String> {
	
	/**
	 * 根据ID进行查询
	 * @param targetId
	 * @return
	 */
	ArboretumTree selectByPkTwo(String targetId);
	
	/**
	 * 拖动修改
	 * @param tree		实体
	 * @throws Exception
	 */
	void insertChangeLevel(ArboretumTree tree) throws Exception;
	
	/**
	 * 添加tree
	 * @param entity ArboretumTree
	 * @throws Exception 
	 */
	void insertPKParentId(ArboretumTree entity) throws Exception;
	
	/**
	 * 删除节点
	 * @param id
	 * @throws Exception 
	 */
	void deleteByPkNew(String id) throws Exception;
	
	/**
	 * 返回所有林业分类(根节点除外)
	 * @return
	 */
	List<ArboretumTree> findAllTree();
}
