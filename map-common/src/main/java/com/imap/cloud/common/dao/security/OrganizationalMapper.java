package com.imap.cloud.common.dao.security;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.imap.cloud.common.dao.base.BaseDao;
import com.imap.cloud.common.entity.security.Organizational;
import com.imap.cloud.common.entity.security.User;

/**
 * 机构DAO
 * @author 冯林
 *
 */
public interface OrganizationalMapper extends BaseDao<Organizational, String> {
	
	/**
	 * 根据ID进行查询主键
	 * @param id
	 * @return
	 */
	Organizational selectByPkTwo(String id);
	
	/**
	 * 修改当前节点的父节点状态
	 * @param parentId
	 */
	void updateByPKParentNode(String parentId);
	
	/**
	 * 删除和用户的中间表
	 * @param id
	 */
	void deleteByPkUser(String id);
	
	/**
	 * 删除子节点
	 * @param list
	 */
	void deleteByPkForEachSubNode(List<String> list);
	
	/**
	 * 加载该机构下的所有用户数据
	 * @param map
	 * @return
	 */
	List<User> selectPKLoadUser(Map<String, Object> map);
	
	/**
	 * 统计该机构下的所有用户数据
	 * @param map
	 * @return
	 */
	int selectPKLoadUserCount(Map<String, Object> map);
	
	/**
	 * 修改 用户禁用和启用的状态
	 * @param userType
	 */
	void UpdateDisabledPKStart(JSONObject userType);
	
	/**
	 * @return	返回机构目录下的所有子节点
	 */
	List<Organizational> findAllSonNode();
	
	/**
	 * 添加机构用户
	 * @param map
	 */
	void insertUser(Map<String, Object> map);

	/**
	 * 返回机构Name是否被使用
	 * @param name 名称
	 * @return
	 */
	int findSonNodeName(String name);

}
