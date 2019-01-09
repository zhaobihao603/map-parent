package com.imap.cloud.common.dto;

import java.util.List;
import java.util.Map;

/**
 * 数据字典用到
 * @author imap
 *
 */
public class Node {
	private String id;   //数据字典项id
	private String text;  //数据字典项的值
	private String className;  //样式
	private Map<String, Object> attributes;// 其他参数  
    private String state;// 是否展开(open,closed)
    private List<Node> children;
    
    public Node(){
    }
    
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
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
	public Map<String, Object> getAttributes() {
		return attributes;
	}
	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public List<Node> getChildren() {
		return children;
	}
	public void setChildren(List<Node> children) {
		this.children = children;
	}
}
