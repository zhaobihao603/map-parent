package com.imap.cloud.common.service.security;

import com.imap.cloud.common.entity.security.Permission;
import com.imap.cloud.common.service.base.BaseService;

/**
 * 权限service接口
 * @author 冯林
 */
public interface PermissionService extends BaseService<Permission, String>{
	
	/**
	 * 物理删除该权限
	 * 同时删除该权限和角色的中间表数据
	 * 同时删除该权限和用户的中间表数据
	 * 同时删除该权限和URL菜单的中间表数据
	 * 
	 * @param id
	 * @throws Exception 
	 */
	void deleteByPkRoleUser(String id) throws Exception;
	
	/**
	 * 
	 * @param permission
	 * @param urlId
	 * @param methodId
	 * @throws Exception
	 */
	void insertPKUrl(Permission permission, String[] urlId,String[] methodId) throws Exception;
	
	/**
	 * 
	 * @param permission
	 * @param urlId
	 * @param methodId
	 * @throws Exception
	 */
	void updateByPkUrl(Permission permission, String[] urlId,String[] methodId) throws Exception;
	
	/**
	 * 查询单条数据
	 * @param id  权限
	 * @return	  
	 * @throws Exception 
	 */
	Permission selectByPkUrl(String id) throws Exception;
	
	/**
	 * 查询权限名称是否被使用
	 * @param name 权限名称
	 * @return 可以使用 ：success 反 error
	 */
	String selectNameByPk(String name);

}
