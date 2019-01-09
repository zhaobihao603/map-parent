package com.imap.cloud.common.service.security.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imap.cloud.common.aop.annotation.LogDescription;
import com.imap.cloud.common.dao.security.UserMapper;
import com.imap.cloud.common.entity.security.Organizational;
import com.imap.cloud.common.entity.security.Permission;
import com.imap.cloud.common.entity.security.Role;
import com.imap.cloud.common.entity.security.User;
import com.imap.cloud.common.service.base.impl.BaseServiceImpl;
import com.imap.cloud.common.service.security.UrlService;
import com.imap.cloud.common.service.security.UserService;
import com.imap.cloud.common.util.AccountUtils;

/**
 * UserDetailsService 接口用来完成 用户与角色关联查询,该接口只有一个方法：loadUserByUsername
 * @author 冯林
 *
 */
@LogDescription(name="用户与角色的服务")
@Service(value="userService")
@SuppressWarnings("unused")
public class UserServiceImpl extends BaseServiceImpl<User,String> implements UserService,UserDetailsService {
	
	private final String ROLE_USER = "caec20f05f0043d4926dd4e3490816at";		//数据库角色表的普通用户角色ID
	
	private UserMapper userMapper;
	
	@Resource
	private UrlService urlService;
	
	@Autowired
	UserServiceImpl (UserMapper userMapper){
		super.setBaseDao(userMapper);
		this.userMapper = userMapper;
	}
	
	/**
	 * loadUserByUsername 查询出来的类型,必须返回 UserDetails
	 */
	@LogDescription(name="加载用户")
	public UserDetails loadUserByUsername(String login)
			throws UsernameNotFoundException{
		User user = null;
		try {
			if(StringUtils.isNotBlank(login)){
				user = userMapper.selectByPk(login);
				if(StringUtils.isNotBlank(user.getId())){
					Set<Role> role = userMapper.selectByPkRole(user.getId());
					if(role != null && role.size() > 0){
						user.setRoleSet(role);
					}
					Set<Permission> permission = userMapper.selectByPkPermission(user.getId());
					if(permission != null && permission.size() > 0){
						user.setPermissionSet(permission);
					}
				}
			}else{
				throw new NullPointerException("没有传入用户账号,登入失败！！");
			}
		} catch (Exception e) {
			throw new NullPointerException("登入失败！！");
		}
		return user;
	}
	
	@Transactional
	@LogDescription(name="删除用户")
	public void deleteByPkRole(String id) throws Exception {
		super.deleteByPk(id);
		userMapper.deletepkRole(id);
		userMapper.deletepkOrganizational(id);
		boolean permissionOpen = false;
		Set<Permission> permission = userMapper.selectByPkPermission(id);
		if(permission != null && permission.size() > 0){
			permissionOpen = true;
		}
		userMapper.deletepkPersonalPermission(id);
		if(permissionOpen){
			urlService.getAllConfigAttributesCallPermission();
		}
	}
	
	@Transactional
	@LogDescription(name="添加用户")
	public void insertPkRole(User user, String[] roleId, String[] personalId,String[] org) throws Exception{
		user.setPassword(AccountUtils.SecurityEncrypt(user.getPassword()));
		super.insert(user);
		//角色权限和个人权限由机构添加和修改，这里只添加默认的一个角色权限
		roleId = new String[1];
		roleId[0] = ROLE_USER;
		//if(roleId != null){
			this.insertRole(user.getId(), roleId);
		//}
		/*if(personalId != null){
			this.insertPersonalPermission(user.getId(), personalId);
		}*/
		if(org != null){
			this.insertOrganization(user.getId(), org);
		}
	}
	
	@Transactional
	@LogDescription(name="修改用户")
	public void updateByPkRole(User user, String[] roleId, String[] personalId,String[] org) throws Exception {
		if(StringUtils.isNotBlank(user.getPassword())){
			if(user.getPassword().length() <= 16){
				user.setPassword(AccountUtils.SecurityEncrypt(user.getPassword()));
			}
		}
		super.updateByPk(user);
		userMapper.deletepkOrganizational(user.getId());
		if(org != null){
			this.insertOrganization(user.getId(), org);
		}
		//角色权限和个人权限由机构添加和修改
		/*if(roleId != null){
			userMapper.deletepkRole(user.getId());
			this.insertRole(user.getId(), roleId);
		}
		if(personalId != null){
			userMapper.deletepkPersonalPermission(user.getId());
			this.insertPersonalPermission(user.getId(), personalId);
		}*/
	}
	
	private void insertRole(String aid,String[] rid){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("aid", aid);
		map.put("rid", rid);
		userMapper.insertPkRole(map);
	}
	
	private void insertPersonalPermission(String aid, String[] pid) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("uid", aid);
		map.put("pid", pid);
		userMapper.insertPersonalPermission(map);
	}
	
	private void insertOrganization(String aid, String[] oid) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("uid", aid);
		map.put("oid", oid);
		userMapper.insertOrganization(map);
	}
	
	public User selectByPkPermission(String login) {
		User user = (User) this.loadUserByUsername(login);
		Set<Organizational>  organizational = userMapper.selectPKOrganizational(user.getId());
		if(organizational != null && organizational.size() > 0){
			user.setOrganizationalSet(organizational);
		}
		return user;
	}
	
	public boolean selectByPkLogin(String login) {
		return userMapper.selectByPkLogin(login) > 0 ? false : true;
	}
	
	/**
	 * 用于去除重复权限 set_
	 * 格式转换 list_
	 * 数据转换 list -》 array_
	 */
	public JSONArray seletePkPermissionRole(Set<Permission> permissionSet,Set<Role> roleSet,String id) throws Exception {
		List<Map<String, Object>> list = null;
		if(!id.equals(AccountUtils.ADMIN)){
			Set<String> set_ = new HashSet<String>();
			List<String> list_ = new ArrayList<String>();
			for (Role role : roleSet) {
				list_.add(role.getR_id());
			}
			List<Map<String,Object>> role = userMapper.selectPkRolePermission(list_);
			int flag = 0;
			for (Map<String, Object> map : role) {
				if(AccountUtils.ROLEADMIN.equals(map.get("r_id"))){
					list = urlService.selectPkPermissionUrl(null);
					flag=1;
				}else{
					set_.add((String)map.get("pid"));
				}
			}
			for (Permission permission : permissionSet) {
				if(AccountUtils.PERMISSIONADMIN.equals(permission.getId())){
					list = urlService.selectPkPermissionUrl(null);
					flag=1;
				}else{
					set_.add(permission.getId());
				}
			}
			if(flag==0){
				list_.clear();
				for (String string : set_) {
					list_.add(string);
				}
				list = urlService.selectPkPermissionUrl(list_);
			}
		}else{
			list = urlService.selectPkPermissionUrl(null);
		}
		List<Map<String, Object>> findAll = urlService.findAllParentNode();
		JSONArray array_ = new JSONArray();
		for (Map<String, Object> url : findAll) {
			JSONArray array = new JSONArray();
			String uid = (String)url.get("u_id");
			for (Map<String, Object> url_ : list) {
				if(uid.equals((String)url_.get("parentId"))){
					JSONObject json = new JSONObject();
					json.put("text", url_.get("url_name"));
					json.put("url", url_.get("address"));
					array.add(json);
				}
			}
			if(array.size() > 0){
				JSONObject json_ = new JSONObject();
				json_.put("text", url.get("url_name"));
				json_.put("className", url.get("className"));
				json_.put("children", array);
				array_.add(json_);
			}
		}
		return array_;
	}
	
	public void getInit(User user,String path) throws Exception{
		JSONArray permissionRole = seletePkPermissionRole(user.getPermissionSet(),user.getRoleSet(),user.getId());
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(new File(path), permissionRole);
	}
	
	public String loadUserMenu(User user){
		try {
			JSONArray permissionRole = seletePkPermissionRole(user.getPermissionSet(),user.getRoleSet(),user.getId());
			return permissionRole.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "[]";
	}

	@Override
	public List<User> findAllUser(String id) {
		Map<String,Object> map = new HashMap<>();
		map.put("id", id);
		map.put("adminID", AccountUtils.ADMIN);
		return userMapper.findAllUser(map);
	}
}
