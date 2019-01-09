package com.imap.cloud.common.entity.teachers;

import java.util.Date;
import java.util.List;

import com.imap.cloud.common.dto.TeachersDTO;
import com.imap.cloud.common.entity.system.SysFile;

public class Teachers {
	
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
   
    //UploadRecord 集合
    private List<SysFile> UploadRecordList;
    
    
    public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

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

	public TeachersDTO toTeachersDTO(){
    	TeachersDTO data = new TeachersDTO();
		data.setId(this.id);
		data.setName(this.name);
		data.setCategoryType(this.categoryType);
		data.setX(this.x);
		data.setY(this.y);
		data.setDescription(this.introduction);
		data.setCreateTime(createTime);
		data.setUpdateTime(updateTime);
		return data;
	}
}