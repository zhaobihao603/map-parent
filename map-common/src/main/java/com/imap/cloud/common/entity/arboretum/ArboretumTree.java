package com.imap.cloud.common.entity.arboretum;

/**
 * 虚拟植物园  林学分类 实体
 * @author 冯林
 *
 */
public class ArboretumTree {
	
	private String id;	   			//自增长主键
	private String text;   			//显示节点文本名称。
	private String state;			//节点状态，'open' 或 'closed'，默认：'open'。如果为'closed'的时候，将不自动展开该节点。
	private String parentId;		//父节点
	
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
		return "ArboretumTree [id=" + id + ", text=" + text + ", state="
				+ state + ", parentId=" + parentId + "]";
	}
	
	public ArboretumTree() {
		super();
	}
	
}
