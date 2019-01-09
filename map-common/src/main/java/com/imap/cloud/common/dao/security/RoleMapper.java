package com.imap.cloud.common.dao.security;

import java.util.Map;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.imap.cloud.common.dao.base.BaseDao;
import com.imap.cloud.common.entity.security.Permission;
import com.imap.cloud.common.entity.security.Role;

/**
 * 
 * 角色Dao接口
 * @author 冯林
 *
 */
public interface RoleMapper extends BaseDao<Role,String>{
	
	/**
	 * 删除角色和权限的中间表
	 * @param id
	 */
	void deleteByPkIntermediatePermission(String id);
	
	/**
	 * 删除角色和用户的中间表
	 * @param id
	 */
	void deleteByPkIntermediateUser(String id);
	
	/**
	 * 保存角色和权限的关系(中间表)
	 * @param map rid: 角色ID pid: 权限数组ID
	 */
	void insertPermission(Map<String, Object> map);
	
	/**
	 * 修改角色信息
	 * @param map rid: 角色ID
	 */
	void deletePermission(String rid);
	
	/**
	 * 查询和角色相关联的权限
	 * @param id 角色ID
	 * @return
	 */
	Set<Permission> selectPkPermission(String id);
	
	/**
	 * 查询角色名称是否被使用
	 * @param response
	 * @param name	角色名称
	 * @param id 
	 */
	int selectNameByPk(@Param("name")String name, @Param("id")String id);
	
	/**
	 * 查询角色标识是否被使用
	 * @param roleName	角色标识
	 * @param id 
	 */
	int selectRoleNameByPk(@Param("roleName")String roleName, @Param("id")String id);
}
