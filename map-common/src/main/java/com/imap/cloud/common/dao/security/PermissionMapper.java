package com.imap.cloud.common.dao.security;

import java.util.Map;
import java.util.Set;

import com.imap.cloud.common.dao.base.BaseDao;
import com.imap.cloud.common.entity.security.MethodClass;
import com.imap.cloud.common.entity.security.Permission;
import org.springframework.stereotype.Repository;

/**
 * 权限DAO接口
 * @author 冯林
 *
 */
@Repository
public interface PermissionMapper extends BaseDao<Permission, String>{
	
	/**
	 * 删除和用户的中间表数据
	 * @param id
	 */
	void deleteByPkUser(String id);
	
	/**
	 * 删除和角色的中间表数据
	 * @param id
	 */
	void deleteByPkRole(String id);
	
	/**
	 * 删除和URL的中间表数据
	 * @param id
	 */
	void deleteByPkUrl(String id);
	
	/**
	 * 删除和方法、类的中间表
	 * @param id
	 */
	void deleteByPkMethodClass(String id);
	
	/**
	 * 添加中间表数据(权限和菜单)
	 * @param map
	 */
	void insertUrl(Map<String, Object> map);
	
	/**
	 * 添加中间表数据(权限和方法、类)
	 * @param map
	 */
	void insertMethodClass(Map<String, Object> map);
	
	/**
	 * 
	 * @param id 权限ID
	 * @return	  返回当前权限对应的方法和类
	 */
	Set<MethodClass> selectByPkUrlMethodClass(String id);

	/**
	 * 查询权限名称是否被使用
	 * @param name 权限名称
	 * @return 可以使用 ：success 反 error
	 */
	int selectNameByPk(String name);
}
