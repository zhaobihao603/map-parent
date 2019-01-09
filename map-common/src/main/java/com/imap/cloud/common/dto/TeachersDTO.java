package com.imap.cloud.common.dto;

import java.util.Date;

public class TeachersDTO {

	
    private String id;     
    private String name;			//教师或实验室名称
    private String deleted;	 		//逻辑删除
    private double x;		 		//对应的X坐标
    private double y;		 		//对应的X坐标
    private String categoryType;    //类别,0为师资，1为实验室
    private String introduction;    //教师或实验室描述
    private String area;    		//校区
    private Date createTime;	//创建时间
    private Date updateTime;	//更改时间
    private String Description;  
    private String imageurl;
    
    public String getImageurl() {
		return imageurl;
	}

	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	/*public List<UploadRecord> getUploadRecordList() {
		return UploadRecordList;
	}

	public void setUploadRecordList(List<UploadRecord> uploadRecordList) {
		UploadRecordList = uploadRecordList;
	}*/

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
        this.name = name == null ? null : name.trim();
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted == null ? null : deleted.trim();
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

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType == null ? null : categoryType.trim();
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction == null ? null : introduction.trim();
    }

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
    
}
