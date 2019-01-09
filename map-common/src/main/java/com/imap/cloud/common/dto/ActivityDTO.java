package com.imap.cloud.common.dto;

import java.util.Date;

public class ActivityDTO {
	
	private String id;			//id
    private String name;		//活动名称
    private String ename;		//活动名称
    private String address;		//活动地址点校区	
    private String enAddress;		//活动地址点校区	
    private Date createdTime;	//创建时间
    private Boolean deleted;	//是否逻辑删除
    private String host;		//校方
    private String speaker;		//主讲
    private String speaktime;		//主办时间
    private Date updatedTime;	//更改时间
    private Double x;			//X坐标
    private Double y;			//Y坐标
    private Boolean fabu;		//发布    true  /没发布   false
    private String username;	//创建用户
    private Boolean audited;
    private Integer fabuState;  //发布状态   0 未发布状态  1已发布状态
    private String area;		//校区
    private String description;	//描述
    private String image;
	
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
	public String getEname() {
		return ename;
	}
	public void setEname(String ename) {
		this.ename = ename;
	}
	public String getEnAddress() {
		return enAddress;
	}
	public void setEnAddress(String enAddress) {
		this.enAddress = enAddress;
	}
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSpeaker() {
		return speaker;
	}
	public void setSpeaker(String speaker) {
		this.speaker = speaker;
	}
	public String getSpeaktime() {
		return speaktime;
	}
	public void setSpeaktime(String speaktime) {
		this.speaktime = speaktime;
	}
	public Date getUpdatedTime() {
		return updatedTime;
	}
	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}
	public Double getX() {
		return x;
	}
	public void setX(Double x) {
		this.x = x;
	}
	public Double getY() {
		return y;
	}
	public void setY(Double y) {
		this.y = y;
	}
	public Boolean getFabu() {
		return fabu;
	}
	public void setFabu(Boolean fabu) {
		this.fabu = fabu;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Boolean getAudited() {
		return audited;
	}
	public void setAudited(Boolean audited) {
		this.audited = audited;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	public ActivityDTO() {
	}
}
