package com.imap.cloud.common.entity.mapLushu;

import java.util.Date;
import java.util.List;

import com.imap.cloud.common.dto.LushuDTO;
import com.imap.cloud.common.entity.system.SysFile;

public class MapLushu {
	private String id; 			//ID
	private String name;			//名称
	private String startTime;		//开始时间
	private String endTime; 	//结束时间
	private String time;      //当天活动时间
	private String endPoint;
	private double x;				//X坐标
	private double y;				//y坐标
	private int seq;   //当天活动排序
	private String lushuid;
	private String path;
	
	
	public String getLushuid() {
		return lushuid;
	}
	public void setLushuid(String lushuid) {
		this.lushuid = lushuid;
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
		this.name = name;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getEndPoint() {
		return endPoint;
	}
	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
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
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}

	

}