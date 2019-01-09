package com.imap.cloud.common.service.security;

import com.imap.cloud.common.entity.security.Role;
import com.imap.cloud.common.service.base.BaseService;

/**
 * 角色Service接口
 * @author 冯林
 *
 */
public interface RoleService extends BaseService<Role,String>{
	
	/**
	 * 删除角色以及
	 * 角色和权限的中间表数据
	 * @param id 角色ID
	 * @throws Exception
	 */
	void deleteByPkIntermediate(String id) throws Exception;
	
	/**
	 * 保存角色信息
	 * 保存角色和权限的信息
	 * @param role		角色实体信息
	 * @param id[]		权限数组ID
	 * @throws Exception
	 */
	void insertPKIntermediate(Role role, String[] id) throws Exception;
	
	/**
	 * 修改当前角色信息
	 * @param role     角色实体信息
	 * @param id[]     权限数组ID
	 * @throws Exception
	 */
	void updateByPkIntermediate(Role role, String[] id) throws Exception;
	
	/**
	 * 返回角色的个人信息
	 * @param id	角色ID
	 * @return
	 * @throws Exception 
	 */
	Role selectByPkPermission(String id) throws Exception;

	/**
	 * 查询角色名称是否被使用
	 * @param response
	 * @param name	角色名称
	 * @param roleName 角色标识
	 * @param id 
	 */
	String selectNameByPk(String name,String roleName, String id);
}
