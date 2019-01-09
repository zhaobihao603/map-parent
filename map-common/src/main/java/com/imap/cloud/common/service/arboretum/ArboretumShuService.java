package com.imap.cloud.common.service.arboretum;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.imap.cloud.common.dto.Pager;
import com.imap.cloud.common.entity.arboretum.ArboretumShu;
import com.imap.cloud.common.service.base.BaseService;

/**
 * 虚拟植物园 树林 service
 * @author 冯林
 *
 */
public interface ArboretumShuService extends BaseService<ArboretumShu, String> {
	
	/**
	 * 删除树木
	 * 同时删除该树木对应的植被规划管理
	 * @param id 树木ID
	 */
	void deleteByPkZbgl(String id) throws Exception;
	
	/**
	 * 
	 * @param treeNode	林学分类查询 Tree树形结构
	 * @param area		校区
	 * @param pageNum
	 * @param pageSize
	 * @return	
	 * @throws Exception 
	 */
	Pager<ArboretumShu> selectPageTreeNode(String treeNode, String area,
			int pageNum, int pageSize) throws Exception;
	
	/**
	 * 用于植被管理，通过xy查询在该区域内的树木
	 * @param xmin
	 * @param ymin
	 * @param xmax
	 * @param ymax
	 * @return
	 * @throws Exception 
	 */
	List<ArboretumShu> findShuData(String xmin, String ymin, String xmax,
			String ymax) throws Exception;
	
	/**
	 * index前端分页（搜索和垂直空间）
	 * @param shu		实体，用于传参
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	Pager<ArboretumShu> selectPageIndex(ArboretumShu shu, int pageNum, int pageSize);
	
	/**
	 * index前端分页（林学Tree使用）
	 * @param treeNode	林学分类查询 Tree树形结构
	 * @param area		校区
	 * @param pageNum
	 * @param pageSize
	 * @return	
	 * @throws Exception 
	 */
	Pager<ArboretumShu> selectPageTreeNodeIndex(String treeNode, String area,int pageNum, int pageSize) throws Exception;

	/**
	 * 更新或者新增
	 * @param shu
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public int save(ArboretumShu shu, HttpServletRequest request)throws Exception;
	
	
}
