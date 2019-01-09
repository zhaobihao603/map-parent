package com.imap.cloud.common.entity.system;

import com.imap.cloud.common.entity.map.MapLayer;


/**
 * 单位(POI及学校单位的实体)
 * @author Bge
 * @since 2016-11-02
 * 
 */
public class Device {
	private String id;
	private String SHAPE;
	private String number;//编号
	private String name;
	private Device parent;//设备种类
	private String status;//状态
	private SysFile image;
	private String standard;//规格
	private MapLayer area;
	
	public MapLayer getArea() {
		return area;
	}
	public void setArea(MapLayer area) {
		this.area = area;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSHAPE() {
		return SHAPE;
	}
	public void setSHAPE(String sHAPE) {
		this.SHAPE = sHAPE == null ? null : sHAPE.trim();
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Device getParent() {
		return parent;
	}
	public void setParent(Device parent) {
		this.parent = parent;
	}
	public SysFile getImage() {
		return image;
	}
	public void setImage(SysFile image) {
		this.image = image;
	}
	public String getStandard() {
		return standard;
	}
	public void setStandard(String standard) {
		this.standard = standard;
	}
	
}
