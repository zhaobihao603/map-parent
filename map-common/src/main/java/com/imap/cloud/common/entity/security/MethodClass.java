package com.imap.cloud.common.entity.security;

import java.io.Serializable;
import java.util.Set;

/**
 * 方法和类实体
 * @author 冯林
 */
public class MethodClass implements Serializable{
	
	private static final long serialVersionUID = -5051921058904402776L;
	
	private String id; 				//主键
	//方法名称规则 /类访问路径/方法访问路径（如:/methodClass/method.do 或 /methodClass/method）
	private String url;				//方法URL或类URL(唯一)
	
	private String name;			//方法名称或类名称
	
	private String interceptType;	//方法拦截类型，0拦截，1不拦截
	
	private String type;			//该URL是方法访问路径，还是类访问路径,0方法1类
	
	private Set<Permission> permissionSet;		//对应的权限实体
	
	public Set<Permission> getPermissionSet() {
		return permissionSet;
	}
	public void setPermissionSet(Set<Permission> permissionSet) {
		this.permissionSet = permissionSet;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getInterceptType() {
		return interceptType;
	}
	public void setInterceptType(String interceptType) {
		this.interceptType = interceptType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
}
