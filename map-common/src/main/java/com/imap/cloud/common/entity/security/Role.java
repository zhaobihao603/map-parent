package com.imap.cloud.common.entity.security;

import java.util.Set;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.GrantedAuthority;

/**
 * 角色实体
 * @author  冯林
 */
public class Role implements GrantedAuthority,ConfigAttribute{
	
	private static final long serialVersionUID = -4149227638690736804L;
	
	private String r_id; 		/* 角色编号,UUID */
	
	private String role_name;	/* 角色编号名称,角色标识 */
	
	private String deleted;		/* 逻辑删除 0 使用 1删除 */	
	
	private String detail;		/* 角色名称  */
	
	private String isEnabled;		/* 该角色是否禁用 0 使用 1禁用 */
	
	private Set<Permission>  permissionSet;	/* 角色和用户的集合 */
	
	public String getDeleted() {
		return deleted;
	}

	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}

	public String getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(String isEnabled) {
		this.isEnabled = isEnabled;
	}

	public String getR_id() {
		return r_id;
	}

	public void setR_id(String r_id) {
		this.r_id = r_id;
	}

	public String getRole_name() {
		return role_name;
	}

	public void setRole_name(String role_name) {
		this.role_name = role_name;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	@Override
	public String toString() {
		return "Role [r_id=" + r_id + ", role_name=" + role_name + ", detail="
				+ detail + ", isEnabled=" + isEnabled + "]";
	}

	public Role() {
		super();
	}

	@Override
	public String getAuthority() {
		return role_name;
	}

	@Override
	public String getAttribute() {
		return role_name;
	}

	public Set<Permission> getPermissionSet() {
		return permissionSet;
	}

	public void setPermissionSet(Set<Permission> permissionSet) {
		this.permissionSet = permissionSet;
	}
	
}