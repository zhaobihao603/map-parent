package com.imap.cloud.common.entity.lushu;

import java.util.Date;
import java.util.List;

import com.imap.cloud.common.dto.LushuDTO;
import com.imap.cloud.common.entity.system.SysFile;

public class Lushu {
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
	private String imageUrl;		//图片路径
	private String startTime;		//开始时间
	private String area;			//校区
	private Boolean fabu;			//发布标示： true--发布; false--没发布
	private String username;		//用户名称
	private Boolean audited;		
	//UploadRecord 集合
	private List<SysFile> UploadRecordList;
	private String xs;
	private String ys;

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
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
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
	public List<SysFile> getUploadRecordList() {
		return UploadRecordList;
	}
	public void setUploadRecordList(List<SysFile> uploadRecordList) {
		UploadRecordList = uploadRecordList;
	}

	public LushuDTO toLushuDTO(){
		LushuDTO dto=new LushuDTO();
		dto.setId(this.id);
		dto.setCreatedTime(this.createdTime);
		dto.setDeleted(this.deleted);
		dto.setEndPoint(this.endPoint);
		dto.setName(this.name);
		dto.setEname(this.ename);
		dto.setUpdatedTime(this.updatedTime);
		dto.setX(this.x);
		dto.setY(this.y);
		dto.setDescription(this.description);
		dto.setLuShu_id(this.luShu_id );
		dto.setStartTime(this.startTime);
		dto.setArea(this.area);
		dto.setFabu(this.fabu);
		dto.setStartTime(this.startTime);
		dto.setUsername(this.username);
		dto.setAudited(this.audited);
		dto.setXs(this.xs);
		dto.setYs(this.ys);
		return dto;
	}
}