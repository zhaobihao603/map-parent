package com.imap.cloud.common.dto;

import java.util.Date;
import java.util.List;

import com.imap.cloud.common.entity.system.SysFile;

public class ScenicDTO {		
	    private String id;       			//id
	    private String name;				//名称
	    private String ename;				//名称
	    private String area;				//校区
	    private Date createtime;			//创建时间
	    private double x;					//X坐标
	    private double y;					//Y坐标
	    private String default_photo_id;	//默认图片ID
	    private String description;			//描述
	    
	    private List<SysFile> UploadRecordList;
	    
		public List<SysFile> getUploadRecordList() {
			return UploadRecordList;
		}
		public void setUploadRecordList(List<SysFile> uploadRecordList) {
			UploadRecordList = uploadRecordList;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getArea() {
			return area;
		}
		public void setArea(String area) {
			this.area = area;
		}
		public Date getCreatetime() {
			return createtime;
		}
		public void setCreatetime(Date createtime) {
			this.createtime = createtime;
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
		public String getDefault_photo_id() {
			return default_photo_id;
		}
		public void setDefault_photo_id(String default_photo_id) {
			this.default_photo_id = default_photo_id;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}	    
}
