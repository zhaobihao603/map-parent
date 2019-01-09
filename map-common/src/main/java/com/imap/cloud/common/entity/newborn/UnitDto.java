package com.imap.cloud.common.entity.newborn;

import java.util.List;

/**
 * 单位Dto(只用于前端新生指引)
 * @author 冯林
 *
 */
public class UnitDto {
	
	private String id;						//主键
	private String name;					//名称
	
	private List<Newstuguideroute> routeList;	//路线
	
	public List<Newstuguideroute> getRouteList() {
		return routeList;
	}
	public void setRouteList(List<Newstuguideroute> routeList) {
		this.routeList = routeList;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
