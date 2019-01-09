package com.imap.cloud.common.entity.school;

import java.util.Date;
import java.util.List;

import com.imap.cloud.common.dto.ActivityDTO;
import com.imap.cloud.common.entity.system.SysFile;

public class Schoolactivity {
   
	private String id;			//id
    private String name;		//活动名称
    private String ename;		//活动名称
    private String address;		//活动地址点校区	
    private String enAddress;		//活动地址点校区	
    private Date createdTime;	//创建时间
    private Boolean deleted;	//是否逻辑删除
    private String host;		//校方
    private String speaker;		//主讲
    private String speaktime;		//主办时间
    private Date updatedTime;	//更改时间
    private Double x;			//X坐标
    private Double y;			//Y坐标
	private String description;	//描述
    private Boolean fabu;		//发布    true  /没发布   false
    private String username;	//创建用户
    private Boolean audited;
    private Integer fabuState;  //发布状态   0 未发布状态  1已发布状态
    private String area;		//校区
    //UploadRecord 集合
    private List<SysFile> UploadRecordList;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getEname() {
		return ename;
	}
	public void setEname(String ename) {
		this.ename = ename;
	}
	public String getEnAddress() {
		return enAddress;
	}
	public void setEnAddress(String enAddress) {
		this.enAddress = enAddress;
	}
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	public Boolean getDeleted() {
		return deleted;
	}
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSpeaker() {
		return speaker;
	}
	public void setSpeaker(String speaker) {
		this.speaker = speaker;
	}
	public String getSpeaktime() {
		return speaktime;
	}
	public void setSpeaktime(String speaktime) {
		this.speaktime = speaktime;
	}
	public Date getUpdatedTime() {
		return updatedTime;
	}
	public void setUpdatedTime(Date updatedTime) {
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Boolean getFabu() {
		return fabu;
	}
	public void setFabu(Boolean fabu) {
		this.fabu = fabu;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Boolean getAudited() {
		return audited;
	}
	public void setAudited(Boolean audited) {
		this.audited = audited;
	}
	public Integer getFabuState() {
		return fabuState;
	}
	public void setFabuState(Integer fabuState) {
		this.fabuState = fabuState;
	}
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
	public Schoolactivity() {
		super();
	}
	public Schoolactivity(String id, String address, Date createdTime,
			Boolean deleted, String host, String name, String speaker,
			String speaktime, Date updatedTime, Double x, Double y,
			String description, Boolean fabu, String username, Boolean audited,
			Integer fabuState, String area, List<SysFile> uploadRecordList) {
		super();
		this.id = id;
		this.address = address;
		this.createdTime = createdTime;
		this.deleted = deleted;
		this.host = host;
		this.name = name;
		this.speaker = speaker;
		this.speaktime = speaktime;
		this.updatedTime = updatedTime;
		this.x = x;
		this.y = y;
		this.description = description;
		this.fabu = fabu;
		this.username = username;
		this.audited = audited;
		this.fabuState = fabuState;
		this.area = area;
		UploadRecordList = uploadRecordList;
	}
	
	public ActivityDTO toActivityDTO(){
		ActivityDTO dto=new ActivityDTO();
		dto.setId(this.id);	
		dto.setAddress(this.address);
		dto.setCreatedTime(this.createdTime);
		dto.setDeleted(this.deleted);
		dto.setHost(this.host);
		dto.setName(this.name);
		dto.setEname(this.ename);
		dto.setEnAddress(this.enAddress);
		dto.setSpeaker(this.speaker);
		dto.setSpeaktime(this.speaktime);
		dto.setUpdatedTime(this.updatedTime);
		dto.setX(this.x);
		dto.setY(this.y);
		dto.setFabu(this.fabu);
		dto.setUsername(this.username);
		dto.setAudited(this.audited);
		dto.setFabuState(this.fabuState);
		dto.setArea(this.area);
		dto.setDescription(this.description);
		return dto;
	}

}