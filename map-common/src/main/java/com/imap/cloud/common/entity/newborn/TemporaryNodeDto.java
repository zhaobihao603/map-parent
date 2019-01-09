package com.imap.cloud.common.entity.newborn;

/**
 * （公共服务咨询点、一卡通充值点、接驳交通车等）只用于前端新生指引
 * @author 冯林
 *
 */
public class TemporaryNodeDto {
	
	private String id;		
	private String name;	
	private String x;
	private String y;
	private String xs;
	private String ys;
	private String layerId;
	private String color;
	private String lineweight;
	
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getLineweight() {
		return lineweight;
	}
	public void setLineweight(String lineweight) {
		this.lineweight = lineweight;
	}
	public String getLayerId() {
		return layerId;
	}
	public void setLayerId(String layerId) {
		this.layerId = layerId;
	}
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
	public String getX() {
		return x;
	}
	public void setX(String x) {
		this.x = x;
	}
	public String getY() {
		return y;
	}
	public void setY(String y) {
		this.y = y;
	}
	public String getXs() {
		return xs;
	}
	public void setXs(String xs) {
		this.xs = xs;
	}
	public String getYs() {
		return ys;
	}
	public void setYs(String ys) {
		this.ys = ys;
	}
	
}
