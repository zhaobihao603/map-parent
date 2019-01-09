package com.imap.cloud.common.dto;

import java.util.Date;

public class VrDTO {
	
	private String id;
	private String name;
	private String ename;
	private String address;
	private String mobile;
	private String image;
	private Double x;
	private Double y;
	private Date dateTime;
	private String actPlace;
	private String description;
	private String url;
	private String mainhtml;
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
	public String getEname() {
		return ename;
	}
	public void setEname(String ename) {
		this.ename = ename;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
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
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getActPlace() {
		return actPlace;
	}
	public void setActPlace(String actPlace) {
		this.actPlace = actPlace;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getMainhtml() {
		return mainhtml;
	}
	public void setMainhtml(String mainhtml) {
		this.mainhtml = mainhtml;
	}	
	    
}
