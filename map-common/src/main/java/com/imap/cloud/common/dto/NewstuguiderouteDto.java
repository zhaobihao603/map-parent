package com.imap.cloud.common.dto;

import java.util.Date;
import java.util.List;

import com.imap.cloud.common.entity.newborn.Newstuguidesite;

public class NewstuguiderouteDto {
	
	private String id;					//id
    private String college_id; 			//学院	
    private Date guide_date;			//创建时间
    private String name;				//名称
    private Boolean audited;			//审计 
    private Boolean fabu;				//发布  0未发布  1 发布
    private Integer fabuState;			//发布状态
    private String area;			//校区
    private String busLine;				//乘车路线
    private String datum;				//携带资料				
    private String reportingTime;		//报到时间		
    private String publicServiceId;    //公共服务咨询点
    private String yktId;				//一卡通充值点
    private String trafficBusId;		//交通车
    private String guideline;			//坐标
    private List<Newstuguidesite> list;  //线路集合
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCollege_id() {
		return college_id;
	}
	public void setCollege_id(String college_id) {
		this.college_id = college_id;
	}
	public Date getGuide_date() {
		return guide_date;
	}
	public void setGuide_date(Date guide_date) {
		this.guide_date = guide_date;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Boolean getAudited() {
		return audited;
	}
	public void setAudited(Boolean audited) {
		this.audited = audited;
	}
	public Boolean getFabu() {
		return fabu;
	}
	public void setFabu(Boolean fabu) {
		this.fabu = fabu;
	}
	public Integer getFabuState() {
		return fabuState;
	}
	public void setFabuState(Integer fabuState) {
		this.fabuState = fabuState;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getBusLine() {
		return busLine;
	}
	public void setBusLine(String busLine) {
		this.busLine = busLine;
	}
	public String getDatum() {
		return datum;
	}
	public void setDatum(String datum) {
		this.datum = datum;
	}
	public String getReportingTime() {
		return reportingTime;
	}
	public void setReportingTime(String reportingTime) {
		this.reportingTime = reportingTime;
	}
	public String getPublicServiceId() {
		return publicServiceId;
	}
	public void setPublicServiceId(String publicServiceId) {
		this.publicServiceId = publicServiceId;
	}
	public String getYktId() {
		return yktId;
	}
	public void setYktId(String yktId) {
		this.yktId = yktId;
	}
	public String getTrafficBusId() {
		return trafficBusId;
	}
	public void setTrafficBusId(String trafficBusId) {
		this.trafficBusId = trafficBusId;
	}
	public String getGuideline() {
		return guideline;
	}
	public void setGuideline(String guideline) {
		this.guideline = guideline;
	}
	public List<Newstuguidesite> getList() {
		return list;
	}
	public void setList(List<Newstuguidesite> list) {
		this.list = list;
	}
    
    
	
}
