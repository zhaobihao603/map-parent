package com.imap.cloud.common.entity.arboretum;

/**
 * 养护记录实体
 * @author 冯林
 *
 */
public class ArboretumYhjl {
	
	private String id;	            	//主键序号
	private String operation;			//操作
	private String remarks;		    	//备注
	private String dateTime;				//日期  
	private String zid;				//植被规划ID
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	public String getZid() {
		return zid;
	}
	public void setZid(String zid) {
		this.zid = zid;
	}
	@Override
	public String toString() {
		return "ArboretumZbglYhjl [id=" + id + ", operation=" + operation
				+ ", remarks=" + remarks + ", dateTime=" + dateTime + ", fzrid="
				+ zid + "]";
	}
	
}
