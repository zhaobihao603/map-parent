package com.imap.cloud.common.service.security;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.imap.cloud.common.entity.security.Organizational;
import com.imap.cloud.common.entity.security.User;
import com.imap.cloud.common.service.base.BaseService;

/**
 * 机构Service
 * @author 冯林
 *
 */
public interface OrganizationalService extends BaseService<Organizational, String>{
	
	/**
	 * 根据ID进行查询主键
	 * @param Id
	 * @return 
	 */
	Organizational selectByPkTwo(String id);
	
	/**
	 * 返回机构Name是否被使用
	 * @return
	 */
	JSONObject findSonNodeName(String name);
	
	/**
	 * Tree拖动保存
	 * @param tree 实体对象
	 * @throws Exception 
	 */
	void insertChangeLevel(Organizational tree) throws Exception;
	
	/**
	 * 删除该机构
	 * 同时删除该机构和用户和中间表数据
	 * @param id
	 * @throws Exception 
	 */
	void deleteByPkUser(String id) throws Exception;
	
	/**
	 * 保存
	 * @param entity
	 * @throws Exception 
	 */
	void insertPKParentId(Organizational entity) throws Exception;
	
	/**
	 * 查询当前机构下的所有用户信息
	 * @param id	机构ID
	 * @param type  判断当前机构是否为父节点
	 * @param rows  当前页
	 * @param page  页大小
	 * @param search 
	 * @return
	 * @throws Exception 
	 */
	List<User> selectPKLoadUser(String id, String type, Integer page, Integer rows, String search) throws Exception;

	/**
	 * 查询统计当前机构下的所有用户信息
	 * @param id	机构ID
	 * @param type	判断当前机构是否为父节点
	 * @param search 
	 * @return
	 * @throws Exception 
	 */
	int selectPKLoadUserCount(String id, String type, String search) throws Exception;
	
	/**
	 * 禁用机构下的用户
	 * @param disabled  禁用类型
	 * @param array 	用户数组
	 */
	void disabledPKUser(JSONObject disabled, JSONArray array);
	
	/**
	 * 启用该机构下的用户
	 * @param start_	启用类型
	 * @param array		用户数组
	 */
	void startPKJSONUser(JSONObject start_, JSONArray array);
	
	/**
	 * 角色选取该机构下的用户  添加角色权限
	 * @param roleJSONArray_   	角色ID
	 * @param userId			用户ID
	 */
	void InsertRoleUser(JSONArray roleJSONArray_, JSONObject userId);
	
	/**
	 * 角色修改该机构下的用户  修改角色权限	
	 * @param roleJSONArray_	角色ID
	 * @param userId			用户ID
	 */
	void UpdateRoleUser(JSONArray roleJSONArray_, JSONObject userId);
	
	/**
	 * 用户个人权限添加
	 * @param permissionJSONArray_		权限ID
	 * @param userId					用户ID
	 */
	void insertPermissionUser(JSONArray permissionJSONArray_, JSONObject userId);
	
	/**
	 * 用户个人权限修改
	 * @param permissionJSONArray_		权限ID
	 * @param userId					用户ID
	 */
	void unsertPermissionUser(JSONArray permissionJSONArray_, JSONObject userId);
	
	/**
	 * @return	返回机构目录下的所有子节点
	 */
	List<Organizational> findAllSonNode();
	
	/**
	 * 添加机构用户
	 * @param uid	用户ID
	 * @param oid	机构ID
	 */
	void insertUser(String uid, String oid);
	
	
}
