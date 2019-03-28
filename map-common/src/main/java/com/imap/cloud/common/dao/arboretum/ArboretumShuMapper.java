package com.imap.cloud.common.dao.arboretum;

import java.util.List;
import java.util.Map;

import com.imap.cloud.common.dao.base.BaseDao;
import com.imap.cloud.common.entity.arboretum.ArboretumShu;
import org.springframework.stereotype.Repository;

/**
 * 虚拟植物园 树林 dao
 * @author 冯林
 *
 */
@Repository
public interface ArboretumShuMapper extends BaseDao<ArboretumShu, String>{
	
	/**
	 * 删除树木和植被规划管理的中间表数据
	 * @param id 树木ID
	 */
	void deleteByPkZbgl(String id);
	
	/**
	 * 用于植被管理，通过xy查询在该区域内的树木
	 * @param xmin
	 * @param ymin
	 * @param xmax
	 * @param ymax
	 * @return
	 */
	List<ArboretumShu> findShuData(Map<String,String> map);
	
	/**
	 * 根据林业分类Tree的ID，进行查询该节点下有没有树木
	 * @param returnList	TreeID
	 * @return 该节点下的树木信息
	 */
	List<ArboretumShu> selectPkTreeNode(Map<String, Object> map);
	
	/**
	 * 根据林业分类Tree的ID，进行查询该节点下有没有树木
	 * @param array returnList	TreeID
	 * @return 返回总数
	 */
	int selectPkTreeNodeCount(List<String> array);
	
	/**
	 * index前端分页（搜索和垂直空间）
	 * @param shu		实体，用于传参
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	List<ArboretumShu> selectPageIndex(Map<String,Object> map);
	
	/**
	 * 统计总数 
	 * @param shu
	 * @return
	 */
	int selectPageIndexCount(ArboretumShu shu);
	
	/**
	 * index前端分页（林学Tree使用）
	 * @param treeNode	林学分类查询 Tree树形结构
	 * @param area		校区
	 * @param pageNum
	 * @param pageSize
	 * @return	
	 * @throws Exception 
	 */
	List<ArboretumShu> selectPageTreeNodeIndex(Map<String,Object> map);
	
	/**
	 * 统计总数 
	   @param treeNode	林学分类查询 Tree树形结构
	 * @param area		校区
	 */
	int selectPageIndexCount(Map<String,Object> map);
	
}