package com.imap.cloud.common.service.security.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imap.cloud.common.aop.annotation.LogDescription;
import com.imap.cloud.common.dao.security.PermissionMapper;
import com.imap.cloud.common.entity.security.MethodClass;
import com.imap.cloud.common.entity.security.Permission;
import com.imap.cloud.common.service.base.impl.BaseServiceImpl;
import com.imap.cloud.common.service.security.PermissionService;
import com.imap.cloud.common.service.security.UrlService;

@LogDescription(name="权限管理")
@Service("permissionServiceImpl")
public class PermissionServiceImpl extends BaseServiceImpl<Permission, String> implements PermissionService {
	
	private PermissionMapper permissionMapper;
	@Resource
	private UrlService urlService;
	
	@Autowired
	PermissionServiceImpl(PermissionMapper permissionMapper){
		super.setBaseDao(permissionMapper);
		this.permissionMapper = permissionMapper;
	}
	
	@Transactional
	@LogDescription(name="删除权限")
	public void deleteByPkRoleUser(String id) throws Exception {
		Permission permission = this.selectByPkUrl(id);
		super.deleteByPk(id);
		permissionMapper.deleteByPkRole(id);
		permissionMapper.deleteByPkUser(id);
		permissionMapper.deleteByPkUrl(id);
		permissionMapper.deleteByPkMethodClass(id);
		//判断当前删除的权限是否以为角色或权限使用，以被使用则删除内存中的权限资源，重新加载权限资源
		if(permission != null &&  permission.getUrlSet() != null && permission.getUrlSet().size() > 0){
			urlService.getAllConfigAttributesCallPermission();
			urlService.getAllConfigAttributesCallRole();
		}
		if(permission != null && permission.getMethodClassSet() != null && permission.getMethodClassSet().size() > 0){
			urlService.getSelectAllMethodClassInterceptUser();
			urlService.getSelectAllMethodClassInterceptRole();
		}
	}

	@Transactional
	@LogDescription(name="添加权限")
	public void insertPKUrl(Permission permission, String[] urlId,String[] methodId)
			throws Exception {
		super.insert(permission);
		if(urlId != null && urlId.length > 0){
			this.insertUrl(permission.getId(), urlId);
			urlService.getAllConfigAttributesCallPermission();
			urlService.getAllConfigAttributesCallRole();
		}
		if(methodId != null && methodId.length > 0){
			this.insertMethodClass(permission.getId(), methodId);
			urlService.getSelectAllMethodClassInterceptUser();
			urlService.getSelectAllMethodClassInterceptRole();
		}
	}

	@Transactional
	@LogDescription(name="修改权限")
	public void updateByPkUrl(Permission permission, String[] urlId,String[] methodId)
			throws Exception {
		super.updateByPk(permission);
		permissionMapper.deleteByPkUrl(permission.getId());
		permissionMapper.deleteByPkMethodClass(permission.getId());
		if(urlId != null && urlId.length > 0){
			this.insertUrl(permission.getId(), urlId);
			urlService.getAllConfigAttributesCallPermission();
			urlService.getAllConfigAttributesCallRole();
		}
		if(methodId != null && methodId.length > 0){
			this.insertMethodClass(permission.getId(), methodId);
			urlService.getSelectAllMethodClassInterceptUser();
			urlService.getSelectAllMethodClassInterceptRole();
		}
	}
	
	private void insertUrl(String pid,String[] uid){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("uid", uid);
		map.put("pid", pid);
		permissionMapper.insertUrl(map);
	}
	
	private void insertMethodClass(String pid,String[] methodId){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("mid", methodId);
		map.put("pid", pid);
		permissionMapper.insertMethodClass(map);
	}
	
	public Permission selectByPkUrl(String id) throws Exception {
		Permission permission = super.selectByPk(id);
		Set<MethodClass> methodClass = permissionMapper.selectByPkUrlMethodClass(id);
		if(methodClass != null && methodClass.size() > 0 && permission != null) permission.setMethodClassSet(methodClass);
		return permission;
	}

	public String selectNameByPk(String name) {
		int count = permissionMapper.selectNameByPk(name);
		if(count > 0){
			return "error";
		}else{
			return "success";
		}
	}
}
