/*
 * MapPass.java
 * Copyright(C) 20xx-2015 xxxxxx��˾
 * All rights reserved.
 * -----------------------------------------------
 * 2017-04-21 Created
 */
package com.imap.cloud.common.entity.map;

/**
 * 
 * 
 * @author ���ܴ���
 * @version 1.0 2017-04-21
 */
public class MapPass {
	private String id;
    private String start;
    private String end;
    /**
     * ͨ��·��(WKT)
     */
    private String path;
    private Double length;
    /**
     * У��
     */
    private String area;
    private String cycleWay;
    private String footWay;
    private String driveWay;
    private String trafficControl;

    public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStart() {
        return start;
    }
    public void setStart(String start) {
        this.start = start == null ? null : start.trim();
    }
    public String getEnd() {
        return end;
    }
    public void setEnd(String end) {
        this.end = end == null ? null : end.trim();
    }
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path == null ? null : path.trim();
    }
    public Double getLength() {
        return length;
    }
    public void setLength(Double length) {
        this.length = length;
    }
    public String getArea() {
        return area;
    }
    public void setArea(String area) {
        this.area = area == null ? null : area.trim();
    }
    public String getCycleWay() {
        return cycleWay;
    }
    public void setCycleWay(String cycleWay) {
        this.cycleWay = cycleWay == null ? null : cycleWay.trim();
    }
    public String getFootWay() {
        return footWay;
    }
    public void setFootWay(String footWay) {
        this.footWay = footWay == null ? null : footWay.trim();
    }
    public String getDriveWay() {
        return driveWay;
    }
    public void setDriveWay(String driveWay) {
        this.driveWay = driveWay == null ? null : driveWay.trim();
    }
    public String getTrafficControl() {
        return trafficControl;
    }
    public void setTrafficControl(String trafficControl) {
        this.trafficControl = trafficControl == null ? null : trafficControl.trim();
    }
    
    @Override
	public String toString() {
		return "MapPass [start=" + this.start + ", end=" + this.end  + ", length=" + this.length + "]";
	}
}