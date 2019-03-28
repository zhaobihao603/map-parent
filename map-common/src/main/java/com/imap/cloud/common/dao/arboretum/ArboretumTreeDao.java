package com.imap.cloud.common.dao.arboretum;

import java.util.List;

import com.imap.cloud.common.dao.base.BaseDao;
import com.imap.cloud.common.entity.arboretum.ArboretumTree;
import org.springframework.stereotype.Repository;

/**
 * 虚拟植物园  林学分类 dao
 * @author 冯林
 *
 */
@Repository
public interface ArboretumTreeDao extends BaseDao<ArboretumTree, String> {
	
	/**
	 * 修改父节点状态
	 * @param parentId
	 */
	void updateByPKParentNode(String parentId);
	
	/**
	 * 根据ID进行查询
	 * @param targetId
	 * @return
	 */
	ArboretumTree selectByPkTwo(String id);
	
	/**
	 * 删除子节点
	 * @param list
	 */
	void deleteByPkForEachSubNode(List<String> list);
}
