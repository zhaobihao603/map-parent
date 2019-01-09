package com.imap.cloud.common.entity.system;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.imap.cloud.common.entity.security.User;

public class Backup {
	private String id;
	private String fileName;
	private String fileUrl;
	private String dbType;
	@JSONField (format="yyyy-MM-dd HH:mm:ss")
	private Date backupDate;
	private String uploader;
	private Boolean deleted;
	private String description;
	private User upld;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileUrl() {
		return fileUrl;
	}
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
	public String getDbType() {
		return dbType;
	}
	public void setDbType(String dbType) {
		this.dbType = dbType;
	}
	public Date getBackupDate() {
		return backupDate;
	}
	public void setBackupDate(Date backupDate) {
		this.backupDate = backupDate;
	}
	public String getUploader() {
		return uploader;
	}
	public void setUploader(String uploader) {
		this.uploader = uploader;
	}
	public Boolean getDeleted() {
		return deleted;
	}
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public User getUpld() {
		return upld;
	}
	public void setUpld(User upld) {
		this.upld = upld;
	}
}
