package com.imap.cloud.common.service.security;

import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSONArray;
import com.imap.cloud.common.entity.security.Permission;
import com.imap.cloud.common.entity.security.Role;
import com.imap.cloud.common.entity.security.User;
import com.imap.cloud.common.service.base.BaseService;

/**
 * 用户Service接口
 * @author 冯林
 *
 */
public interface UserService extends BaseService<User,String>{
	
	/**
	 * 逻辑删除用户
	 * 删除用户和角色的中间表数据
	 * @param 用户id
	 */
	void deleteByPkRole(String id) throws Exception;
	
	/**
	 * 保存用户信息
	 * 保存用户和角色和中间表数据
	 * @param user			用户数据
	 * @param personalId    个人权限Id数组
	 * @param org           机构ID
	 * @param roleId[]		角色ID	
	 */
	void insertPkRole(User user, String[] roleId, String[] personalId, String[] org)throws Exception;
	
	/**
	 * 修改用户信息
	 * 修改用户和角色的中间表数据
	 * @param user	               用户数据
	 * @param personalId  个人权限Id数组
	 * @param org         机构ID
	 * @param roleId[]	     角色ID	
	 * @throws Exception 
	 */
	void updateByPkRole(User user, String[] roleId, String[] personalId, String[] org) throws Exception;
	
	/**
	 * 根据用户账号查询当前用户的信息
	 * @param login	用户账号
	 * @return	用户当前信息
	 */
	User selectByPkPermission(String login);
	
	/**
	 * 根据当前用户的权限，返回当前用户的菜单列表
	 * @param permissionSet	个人权限
	 * @param roleSet		角色权限
	 * @return	JSON
	 * @throws Exception 
	 */
	JSONArray seletePkPermissionRole(Set<Permission> permissionSet,Set<Role> roleSet,String id) throws Exception;
	
	/**
	 * 查询当前添加用户账号是否以被使用
	 * @param login	账号名
	 * @return	true 可以使用，false不能使用
	 */
	boolean selectByPkLogin(String login);
	
	/**
	 * 用户登入成功后
	 * 根据当前用户的(个人权限和角色权限)进行页面菜单的加载
	 * 生成 (用户账号.json)文件。
	 * @throws Exception 
	 */
	void getInit(User user, String path) throws Exception;
	
	String loadUserMenu(User user);
	
	/**
	 * @param id	机构ID
	 * @return		查询所有用户信息(除去当前机构以有的用户信息)
	 */
	List<User> findAllUser(String id);	

	
}
