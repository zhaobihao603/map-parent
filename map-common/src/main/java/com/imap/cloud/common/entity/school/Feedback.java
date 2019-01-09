package com.imap.cloud.common.entity.school;

import java.io.Serializable;

/**
 * 反馈实体
 * @author 冯林
 *
 */
public class Feedback implements Serializable{
	private static final long serialVersionUID = 8080563498553928998L;
	private String id;			//主键
	private String title;		//标题
	private String type;		//反馈类型(保存数据字典中)
	private String createDate;	//创建时间
	private String content;		//反馈内容
	private String imageUrl;	//反馈图片
	
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	
}
