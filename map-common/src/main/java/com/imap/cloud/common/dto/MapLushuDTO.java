package com.imap.cloud.common.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.imap.cloud.common.entity.mapLushu.MapLushu;


public class MapLushuDTO {

	private String id; 			//ID
	private String name;			//名称
	private String startTime;		//开始时间
	private String endTime; 	//结束时间
	private String path;	//xy
	private List<DateMapLushuDTO> list ;
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
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public List<DateMapLushuDTO> getList() {
		if(this.list ==null)
			return new ArrayList<DateMapLushuDTO>();
		else
			return list;
	}
	public void setList(List<DateMapLushuDTO> list) {
		this.list = list;
	}


}
