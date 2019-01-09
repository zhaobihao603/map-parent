package com.imap.cloud.common.entity.security;

import java.io.Serializable;
import java.util.Set;

import org.springframework.security.access.ConfigAttribute;

/**
 * 权限 
 * @author 冯林
 */

public class Permission implements Serializable {

	private static final long serialVersionUID = -7209742155618625292L;
	private String id;						//主键
	private String per_name;				//权限名称
	private String deleted;					//逻辑删除
	private Set<ConfigAttribute> roleSet;	//对应的角色集合
	private Set<User> userSet;				//对应的用户集合
	private Set<Url> urlSet;				//对应的菜单集合
	private Set<MethodClass> methodClassSet;//对应的方法、类集合
	
	public Set<MethodClass> getMethodClassSet() {
		return methodClassSet;
	}

	public void setMethodClassSet(Set<MethodClass> methodClassSet) {
		this.methodClassSet = methodClassSet;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Set<ConfigAttribute> getRoleSet() {
		return roleSet;
	}

	public void setRoleSet(Set<ConfigAttribute> roleSet) {
		this.roleSet = roleSet;
	}

	/** default constructor */
	public Permission() {
	}

	/** full constructor */
	public Permission(String per_name) {
		this.per_name = per_name;
	}

	public String getPer_name() {
		return per_name;
	}

	public void setPer_name(String per_name) {
		this.per_name = per_name;
	}
	
	public Set<User> getUserSet() {
		return userSet;
	}

	public void setUserSet(Set<User> userSet) {
		this.userSet = userSet;
	}
	
	public Set<Url> getUrlSet() {
		return urlSet;
	}

	public void setUrlSet(Set<Url> urlSet) {
		this.urlSet = urlSet;
	}

	@Override
	public String toString() {
		return "Permission [id=" + id + ", per_name=" + per_name + "]";
	}

	public String getDeleted() {
		return deleted;
	}

	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}

	
}