package com.imap.cloud.common.entity.security;

import java.io.Serializable;

/**
 * 机构实体
 * @author 冯林
 *
 */
public class Organizational implements Serializable{

	private static final long serialVersionUID = -117663396445385048L;
	
	private String id;	   			//主键
	private String text;   			//显示节点文本名称。
	private String state;			//节点状态，'open' 或 'closed' 子节点，closed 父节点。
	private String parentId;		//父节点
	
	//private Set<User> userSet;		//该机构下的用户集合 
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	@Override
	public String toString() {
		return "Organizational [id=" + id + ", text=" + text + ", state="
				+ state + ", parentId=" + parentId + "]";
	}
	/*public Set<User> getUserSet() {
		return userSet;
	}
	public void setUserSet(Set<User> userSet) {
		this.userSet = userSet;
	}*/
	
}
