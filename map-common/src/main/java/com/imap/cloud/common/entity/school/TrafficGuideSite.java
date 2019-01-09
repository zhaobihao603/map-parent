package com.imap.cloud.common.entity.school;

import java.io.Serializable;

/**
 * 交通查询节点实体
 * @author 冯林
 *
 */
public class TrafficGuideSite implements Serializable{
	
	private static final long serialVersionUID = 3751872447760005433L;
	
	private String id;			//主键
	private String name;		//名称
	private String detail;		//备注
	private String x;			//x
	private String y;			//y
	private String route_id;	//交通查询实体ID
	
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
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String getX() {
		return x;
	}
	public void setX(String x) {
		this.x = x;
	}
	public String getY() {
		return y;
	}
	public void setY(String y) {
		this.y = y;
	}
	public String getRoute_id() {
		return route_id;
	}
	public void setRoute_id(String route_id) {
		this.route_id = route_id;
	}
	
	
}
