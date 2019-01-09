package com.imap.cloud.common.entity.lushu;

import com.imap.cloud.common.dto.LushupathDto;


public class Lushupath {
    
	private String id;				
    private String createdTime;	//创建时间
    private Boolean deleted;	//逻辑删除
    private String name;		//名称
    private String startPoint;	//开始点
    private String updatedTime;	//修改时间
    private Double x;			//x坐标
    private Double y;			//y坐标
    private String lushuId;	//关联ID
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

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint == null ? null : startPoint.trim();
    }

    public String getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
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

    public String getLushuId() {
        return lushuId;
    }

    public void setLushuId(String lushuId) {
        this.lushuId = lushuId;
    }
    
    public LushupathDto toLushupathDto(){
    	LushupathDto date=new LushupathDto();
    	date.setCreatedTime(createdTime);
    	date.setDeleted(deleted);
    	date.setId(id);
    	date.setLushuId(lushuId);
    	date.setName(name);
    	date.setStartPoint(startPoint);
    	date.setUpdatedTime(updatedTime);
    	return date;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
}