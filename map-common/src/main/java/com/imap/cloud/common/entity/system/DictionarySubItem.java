package com.imap.cloud.common.entity.system;

/**
 * @since 2016-10-19 
 * @author 赵毕皓
 */
public class DictionarySubItem {
	
	private String id;  //叶子节点属性ID uuid
	private DictionaryItem item; //关联的叶子节点
	private String code;        //编码
	private String value;       //值
	private String shortvalue;  //简称
	private Integer ordervalue; //序号
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public DictionaryItem getItem() {
		return item;
	}
	public void setItem(DictionaryItem item) {
		this.item = item;
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
	public String getShortvalue() {
		return shortvalue;
	}
	public void setShortvalue(String shortvalue) {
		this.shortvalue = shortvalue;
	}
	public Integer getOrdervalue() {
		return ordervalue;
	}
	public void setOrdervalue(Integer ordervalue) {
		this.ordervalue = ordervalue;
	}
}
