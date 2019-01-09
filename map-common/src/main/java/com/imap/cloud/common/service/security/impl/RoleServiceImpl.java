package com.imap.cloud.common.service.security.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imap.cloud.common.aop.annotation.LogDescription;
import com.imap.cloud.common.dao.security.RoleMapper;
import com.imap.cloud.common.entity.security.Permission;
import com.imap.cloud.common.entity.security.Role;
import com.imap.cloud.common.service.base.impl.BaseServiceImpl;
import com.imap.cloud.common.service.security.RoleService;
import com.imap.cloud.common.service.security.UrlService;

@LogDescription(name="角色管理")
@Service("roleServiceImpl")
public class RoleServiceImpl extends BaseServiceImpl<Role,String> implements RoleService {
	
	private RoleMapper roleMapper;
	@Resource
	private UrlService urlService;
	
	@Autowired
	RoleServiceImpl(RoleMapper roleMapper){
		super.setBaseDao(roleMapper);
		this.roleMapper = roleMapper;
	}
	
	@Transactional
	@LogDescription(name="删除角色")
	public void deleteByPkIntermediate(String id) throws Exception{
		super.deleteByPk(id);
		roleMapper.deleteByPkIntermediateUser(id);
		Set<Permission> permission = roleMapper.selectPkPermission(id);
		boolean open = false;
		if(permission != null && permission.size() > 0){
			open = true;
		}
		roleMapper.deleteByPkIntermediatePermission(id);
		if(open == true){
			urlService.getAllConfigAttributesCallRole();
		}
	}
	
	@Transactional
	@LogDescription(name="添加角色")
	public void insertPKIntermediate(Role role, String[] id) throws Exception {
		super.insert(role);
		if(id != null){
			this.insertPermission(role.getR_id(), id);
			urlService.getAllConfigAttributesCallRole();
		}
	}

	@Transactional
	@LogDescription(name="修改角色")
	public void updateByPkIntermediate(Role role, String[] id) throws Exception {
		super.updateByPk(role);
		roleMapper.deleteByPkIntermediatePermission(role.getR_id());
		if(id != null){
			this.insertPermission(role.getR_id(), id);
			urlService.getAllConfigAttributesCallRole();
		}
	}
	
	private void insertPermission(String rid,String[] id){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rid", rid);
		map.put("pid",id);
		roleMapper.insertPermission(map);
	}

	public Role selectByPkPermission(String id) throws Exception {
		Role role = super.selectByPk(id);
		Set<Permission> permission= roleMapper.selectPkPermission(id);
		if(permission != null && permission.size() > 0) role.setPermissionSet(permission);
		return role;
	}

	public String selectNameByPk(String name,String roleName,String id) {
		int count = roleMapper.selectNameByPk(name,id);
		int count1 = roleMapper.selectRoleNameByPk(roleName,id);
		if(count > 0 || count1>0){
			return "error";
		}else{
			return "success";
		}
	}
}
