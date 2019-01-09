package com.imap.cloud.common.entity.school;

import java.io.Serializable;
import java.util.List;

/**
 * 交通查询实体
 * @author 冯林
 *
 */
public class TrafficGuideRoute implements Serializable{
	
	private static final long serialVersionUID = 7358807959165530145L;
	
	private String id;				//主键
	private String name;			//名称
	private String trafficLine; 	//坐标
	private String trafficType; 	//乘车类型  公交、的士
	private String busLine;			//乘车路线
	private String startStation;	//起点
	private String endStation;		//终点
	private String area;			//校区
	
	private List<TrafficGuideSite> sites;	//交通查询节点
	
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public List<TrafficGuideSite> getSites() {
		return sites;
	}
	public void setSites(List<TrafficGuideSite> sites) {
		this.sites = sites;
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
	public String getTrafficLine() {
		return trafficLine;
	}
	public void setTrafficLine(String trafficLine) {
		this.trafficLine = trafficLine;
	}
	public String getTrafficType() {
		return trafficType;
	}
	public void setTrafficType(String trafficType) {
		this.trafficType = trafficType;
	}
	public String getBusLine() {
		return busLine;
	}
	public void setBusLine(String busLine) {
		this.busLine = busLine;
	}
	public String getStartStation() {
		return startStation;
	}
	public void setStartStation(String startStation) {
		this.startStation = startStation;
	}
	public String getEndStation() {
		return endStation;
	}
	public void setEndStation(String endStation) {
		this.endStation = endStation;
	}
	
	
}
