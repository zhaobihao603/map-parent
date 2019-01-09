package com.imap.cloud.common.entity.system;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.imap.cloud.common.entity.security.User;

public class SysFile {
	private String id; 		//上传文件主键ID
    private String module;		//上传模块
    private String fileName;	//上传文件原来的名称
    private String fileFormat;	//上传文件类型
    private String fileUrl;		//上传文件存储路径
    private Long fileSize;	//上传文件大小
    private String realName;	//上传文件加密后的名称
    private String uploader;	//上传人
    @JSONField (format="yyyy-MM-dd HH:mm:ss")
    private Date uploadDate;	//上传文件时间
    private String description;	//描述
    private String title;       //标题（景点）
    private Boolean deleted;	//删除标识  1-已删除  0-未删除
    private User upld;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName == null ? null : realName.trim();
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName == null ? null : fileName.trim();
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl == null ? null : fileUrl.trim();
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileFormat() {
        return fileFormat;
    }

    public void setFileFormat(String fileFormat) {
        this.fileFormat = fileFormat == null ? null : fileFormat.trim();
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module == null ? null : module.trim();
    }

    public String getUploader() {
        return uploader;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader == null ? null : uploader.trim();
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
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
        this.description = description == null ? null : description.trim();
    }

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
    
	public User getUpld() {
		return upld;
	}
	
	public void setUpld(User upld) {
		this.upld = upld;
	}
}
