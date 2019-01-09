package com.imap.cloud.common.entity.arboretum;

import java.util.List;

/**
 * 植被规划管理实体
 * @author 冯林
 *
 */
public class ArboretumZbgl {
	
	private String id;						//主键序号
	private String personCharge;			//负责人
	private String xs;						//x多路径
	private String ys;						//y多路径
	private String x;						//x路径（用于定位，第一个坐标点）
	private String y;						//y路径（用于定位，第一个坐标点）
	private String area;		    		//校区  
	private String shuId;					//用于接收养护记录
	private List<ArboretumYhjl> yhjlList;	//养护记录
	
	public List<ArboretumYhjl> getYhjlList() {
		return yhjlList;
	}
	public void setYhjlList(List<ArboretumYhjl> yhjlList) {
		this.yhjlList = yhjlList;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPersonCharge() {
		return personCharge;
	}
	public void setPersonCharge(String personCharge) {
		this.personCharge = personCharge;
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
	public String getX() {
		return x;
	}
	public void setX(String x) {
		this.x = x;
	}
	public String getShuId() {
		return shuId;
	}
	public void setShuId(String shuId) {
		this.shuId = shuId;
	}
	public String getY() {
		return y;
	}
	public void setY(String y) {
		this.y = y;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	
}
