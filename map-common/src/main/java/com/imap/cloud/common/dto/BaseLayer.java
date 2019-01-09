package com.imap.cloud.common.dto;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

public abstract class BaseLayer {
	
	private String id;
    /**
     */
    private String name;
    /**
     */
    private String ename;
    /**
     * 数据别名
     */
    private String alias;
    /**
     * 数据来源
     */
    private String source;
    /**
     * WMS、WFS、自定义
     */
    private String layerType;
    /**
     * 地图角度
     */
    private String angle;
    /**
     * 排序
     */
    private Integer seq;
    /**
     * 是否显示
     */
    private Boolean display;
    /**
     * 状态
     */
    private Boolean status;
    /**
     * 是否已删除
     */
    private Boolean deleted;
    /**
     * 添加日期
     */
    @JSONField (format="yyyy-MM-dd HH:mm:ss")
    private Date addDate;
    /**
     * 备注
     */
    private String remark;
    /**
     * 放置目录的id
     */
    private String dirId;
    
	public String getDirId() {
		return dirId;
	}
	public void setDirId(String dirId) {
		this.dirId = dirId;
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
	public String getEname() {
		return ename;
	}
	public void setEname(String ename) {
		this.ename = ename;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getLayerType() {
		return layerType;
	}
	public void setLayerType(String layerType) {
		this.layerType = layerType;
	}
	public String getAngle() {
		return angle;
	}
	public void setAngle(String angle) {
		this.angle = angle;
	}
	public Integer getSeq() {
		return seq;
	}
	public void setSeq(Integer seq) {
		this.seq = seq;
	}
	public Boolean getDisplay() {
		return display;
	}
	public void setDisplay(Boolean display) {
		this.display = display;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public Boolean getDeleted() {
		return deleted;
	}
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
	public Date getAddDate() {
		return addDate;
	}
	public void setAddDate(Date addDate) {
		this.addDate = addDate;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

}
