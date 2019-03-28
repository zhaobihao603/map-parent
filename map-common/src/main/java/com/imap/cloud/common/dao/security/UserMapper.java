package com.imap.cloud.common.dao.security;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.imap.cloud.common.dao.base.BaseDao;
import com.imap.cloud.common.entity.security.Organizational;
import com.imap.cloud.common.entity.security.Permission;
import com.imap.cloud.common.entity.security.Role;
import com.imap.cloud.common.entity.security.User;
import org.springframework.stereotype.Repository;

/**
 * 
 * 用户接口
 * @author 冯林
 *
 */
@Repository
public interface UserMapper extends BaseDao<User,String> {
	
	/**
	 * @param id 	登入用户账号Id
	 * @return		当前用户的个人权限
	 */
	Set<Permission> selectByPkPermission(String id);
	
	/**
	 * 删除用户和角色的中间表数据
	 * @param id
	 */
	void deletepkRole(String id);
	
	/**
	 * 删除用户和机构的关系
	 * @param id
	 */
	void deletepkOrganizational(String id);
	
	/**
	 * 删除当前用户的个人权限(中间表)
	 * @param id
	 */
	void deletepkPersonalPermission(String id);
	
	/**
	 * 添加用户和角色中间表数据
	 * @param map
	 */
	void insertPkRole(Map<String, Object> map);
	
	/**
	 * 
	 * @param id  登入用户账号Id
	 * @return    当前用户的角色权限
	 */
	Set<Role> selectByPkRole(String id);
	
	/**
	 * 
	 * @param list 当前用户角色集合
	 * @return	返回该角色集合对应权限
	 */
	List<Map<String, Object>> selectPkRolePermission(
			List<String> list);
	
	/**
	 * 添加当前用户的个人权限(中间表)
	 * @param map
	 */
	void insertPersonalPermission(Map<String, Object> map);
	
	/**
	 * 查询当前添加用户账号是否以被使用
	 * @param login	账号名
	 * @return	true 可以使用，false不能使用
	 */
	int selectByPkLogin(String login);
	
	/**
	 * 添加当前用户的机构(中间表)
	 * @param map
	 */
	void insertOrganization(Map<String, Object> map);
	
	/**
	 * 根据用户ID去查询和机构的关系
	 * @param id
	 * @return
	 */
	Set<Organizational> selectPKOrganizational(String id);
	
	/**
	 * @param adminID	超级管理员用户ID，不会查询出来
	 * @param id 		机构ID
	 * @return   		查询所有用户信息(除去当前机构以有的用户信息)
	 */
	List<User> findAllUser(Map<String, Object> map);
	
}
