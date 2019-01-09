package com.imap.cloud.common.entity.security;

import java.io.Serializable;
import java.util.Set;

/**
 * Url 资源实体
 * @author 冯林
 *
 */
public class Url implements Serializable{
	private static final long serialVersionUID = 5401776652896559058L;

	private String id;							//主键
	private String address;						//Url地址
	private String dispaly;						//是否显示 0 显示 1不显示
	private Integer orderValue;					//排序
	private String urlname;						//Url名称
	private String ename;
	private String template;					//模板
	private String className;					//父节点菜单对应的图片样式
	private String parentId;					//菜单父ID
	private String code;						//前台父节点编号
	private Set<Permission> permissionSet;		//对应的权限实体
	
	public String getTemplate() {
		return template;
	}
	public void setTemplate(String template) {
		this.template = template;
	}
	public Url() {
		super();
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Set<Permission> getPermissionSet() {
		return permissionSet;
	}
	public void setPermissionSet(Set<Permission> permissionSet) {
		this.permissionSet = permissionSet;
	}
	public String getDispaly() {
		return dispaly;
	}
	public void setDispaly(String dispaly) {
		this.dispaly = dispaly;
	}
	public Integer getOrderValue() {
		return orderValue;
	}
	public void setOrderValue(Integer orderValue) {
		this.orderValue = orderValue;
	}
	public String getUrlname() {
		return urlname;
	}
	public void setUrlname(String urlname) {
		this.urlname = urlname;
	}
	public String getEname() {
		return ename;
	}
	public void setEname(String ename) {
		this.ename = ename;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	@Override
	public String toString() {
		return "Url [id=" + id + ", address=" + address + ", dispaly="
				+ dispaly + ", orderValue=" + orderValue + ", urlname="
				+ urlname + ", template=" + template + ", className="
				+ className + ", parentId=" + parentId + ", code=" + code + "]";
	}
	
	
	
}
