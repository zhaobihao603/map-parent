package com.imap.cloud.common.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.imap.cloud.common.entity.mapLushu.MapLushu;


public class DateMapLushuDTO {

	private String id; 			//ID
	private String time;			//当前日期
	private String lushuid;
	private List<MapLushu> list ;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public List<MapLushu> getList() {
		if(this.list == null)
			return new ArrayList<MapLushu>();
		else
			return list;
	}
	public void setList(List<MapLushu> list) {
		this.list = list;
	}
	public String getLushuid() {
		return lushuid;
	}
	public void setLushuid(String lushuid) {
		this.lushuid = lushuid;
	}
	

}
