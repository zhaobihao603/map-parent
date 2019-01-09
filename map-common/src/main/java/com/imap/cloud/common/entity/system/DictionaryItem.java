package com.imap.cloud.common.entity.system;

import java.util.List;

/**
 * @since 2016-10-19 
 * @author 赵毕皓
 */
public class DictionaryItem {
	
	private String id;//主键
	private String code;//字段编码
	private String value;//值
	private String parentid;//父节点
	private String shortvalue; //简称
	private String datatype;   //数据类型
	private Boolean isLeaf;    //是否为叶子节点
	private String state;      //状态
	private List<DictionarySubItem> lstSubItem;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getParentid() {
		return parentid;
	}
	public void setParentid(String parentid) {
		this.parentid = parentid;
	}
	public String getShortvalue() {
		return shortvalue;
	}
	public void setShortvalue(String shortvalue) {
		this.shortvalue = shortvalue;
	}
	public String getDatatype() {
		return datatype;
	}
	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}
	public Boolean getIsLeaf() {
		return isLeaf;
	}
	public void setIsLeaf(Boolean isLeaf) {
		this.isLeaf = isLeaf;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public List<DictionarySubItem> getLstSubItem() {
		return lstSubItem;
	}
	public void setLstSubItem(List<DictionarySubItem> lstSubItem) {
		this.lstSubItem = lstSubItem;
	}
}
