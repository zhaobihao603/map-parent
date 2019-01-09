package com.imap.cloud.common.entity.vr;

import java.util.List;

import com.imap.cloud.common.dto.VrDTO;
import com.imap.cloud.common.entity.system.SysFile;

public class Vr {
	
    private String id;				//id
    private String name;			//vr名称
    private String ename;			//vr名称
    private String vrtype;			//VR类型
    private String description;		//描述
    private String imageurl;		//图片URL
    private String mainhtml;		//默认页面
    private String vruploadUrl;	//文件上传
    private Double x;				//X坐标
    private Double y;				//Y坐标
    private String deleted;			//逻辑删除标示：0未删除，1删除
    private String area;			//校区
    //UploadRecord 集合
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

    public String getVrtype() {
        return vrtype;
    }

    public void setVrtype(String vrtype) {
        this.vrtype = vrtype == null ? null : vrtype.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl == null ? null : imageurl.trim();
    }

    public String getMainhtml() {
        return mainhtml;
    }

    public void setMainhtml(String mainhtml) {
        this.mainhtml = mainhtml == null ? null : mainhtml.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

   
    public String getEname() {
		return ename;
	}

	public void setEname(String ename) {
		this.ename = ename;
	}

	public String getVruploadUrl() {
		return vruploadUrl;
	}

	public void setVruploadUrl(String vruploadUrl) {
		this.vruploadUrl = vruploadUrl;
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

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted == null ? null : deleted.trim();
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
    
    public VrDTO toVrDTO(){
    	VrDTO dto=new VrDTO();
    	dto.setId(this.id.toString());
    	dto.setName(this.name);
    	dto.setEname(this.ename);
    	dto.setX(this.x);
    	dto.setY(this.y);
    	dto.setDescription(this.description);
    	dto.setMainhtml(this.mainhtml);
    	return dto;
    }
    
    
}