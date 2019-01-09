package com.imap.cloud.common.dto;

import java.util.Date;


public class LushuDTO {

	private String id; 			//ID
	private Date createdTime;		//创建时间
	private Boolean deleted;		//逻辑删除
	private String endPoint;		//结束指向
	private String name;			//名称
	private String ename;			//名称
	private Date updatedTime; 	//修改时间
	private double x;				//X坐标
	private double y;				//y坐标
	private String description;		//描述  
	private String luShu_id;		//路书ID
	private String image;
	private String imageUrls;		//图片路径
	private String startTime;		//开始时间
	private String area;			//校区
	private Boolean fabu;			//发布标示： true--发布; false--没发布
	private String username;		//用户名称
	private Boolean audited;		
	private String xs;
	private String ys;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getEndPoint() {
		return endPoint;
	}
	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
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
	public Date getUpdatedTime() {
		return updatedTime;
	}
	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLuShu_id() {
		return luShu_id;
	}
	public void setLuShu_id(String luShu_id) {
		this.luShu_id = luShu_id;
	}

	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
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
	public String getXs() {
		return xs;
	}
	public void setXs(String xs) {
		this.xs = xs;
	}
	public String getYs() {
		return ys;
	}
	public void setYs(String ys) {
		this.ys = ys;
	}

	public String getImage() {
		return image;
	}
	
	public void setImage(String image) {
		this.image = image;
	}
	
	public String getImageUrls() {
		return imageUrls;
	}
	public void setImageUrls(String imageUrls) {
		this.imageUrls = imageUrls;
	}
	public LushuDTO() {

	}
	public LushuDTO(String id, Date createdTime, Boolean deleted,
			String endPoint, String name,String ename, Date updatedTime, double x,
			double y, String description, String luShu_id,
			String imageUrls, String startTime, String area, 
			String username, Boolean audited, Boolean fabu, String xs,
			String ys) {
		super();
		this.id = id;
		this.createdTime = createdTime;
		this.deleted = deleted;
		this.endPoint = endPoint;
		this.name = name;
		this.ename = ename;
		this.updatedTime = updatedTime;
		this.x = x;
		this.y = y;
		this.description = description;
		this.luShu_id = luShu_id;
		this.imageUrls = imageUrls;
		this.startTime = startTime;
		this.area = area;
		this.fabu = fabu;
		this.username = username;
		this.audited = audited;
		this.xs = xs;
		this.ys = ys;
	}
}
