/*
 * LayerService.java
 * Copyright(C) 20xx-2015 xxxxxx��˾
 * All rights reserved.
 * -----------------------------------------------
 * 2016-10-18 Created
 */
package com.imap.cloud.common.entity.map;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 
 * 
 * @author  Hxin
 * @version 1.0 2016-10-18
 */
public class MapLayer {

    private String id;
    /**
     */
    private String name;
    /**
     */
    private String ename;
    /**
     */
    private String alias;
    /**
     */
    private String url;
    /**
     */
    private String source;
    /**
     */
    private String layerType;
    /**
     */
    private String featureNS;
    /**
     */
    private String featureType;
    /**
     */
    private String version;
    /**
     */
    private String resolution;
    /**
     */
    private String projection;
    /**
     * 地图范围
     */
    private String bound;
    /**
     * 图层范围（由此可以计算图层中心点）
     */
    private String extent;
    /**
     */
    private String layers;
    /**
     */
    private String tilesize;
    /**
     */
    private String tileorigin;
    /**
     */
    private String angle;
    /**
     */
    private Integer seq;
    /**
     * 
     */
    private Boolean display;
    /**
     */
    private Boolean status;
    /**
     */
    private Boolean deleted;
    @JSONField (format="yyyy-MM-dd HH:mm:ss")
    private Date addDate;
    /**
     */
    private String remark;
    
    private String dirId;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }
    public String getEname() {
        return ename;
    }
    public void setEname(String ename) {
        this.ename = ename == null ? null : ename.trim();
    }
    public String getAlias() {
        return alias;
    }
    public void setAlias(String alias) {
        this.alias = alias == null ? null : alias.trim();
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }
    public String getSource() {
        return source;
    }
    public void setSource(String source) {
        this.source = source == null ? null : source.trim();
    }
    public String getLayerType() {
        return layerType;
    }
    public void setLayerType(String layerType) {
        this.layerType = layerType == null ? null : layerType.trim();
    }
    public String getFeatureNS() {
        return featureNS;
    }
    public void setFeatureNS(String featureNS) {
        this.featureNS = featureNS == null ? null : featureNS.trim();
    }
    public String getFeatureType() {
        return featureType;
    }
    public void setFeatureType(String featureType) {
        this.featureType = featureType == null ? null : featureType.trim();
    }
    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version == null ? null : version.trim();
    }
    public String getResolution() {
        return resolution;
    }
    public void setResolution(String resolution) {
        this.resolution = resolution == null ? null : resolution.trim();
    }
    public String getProjection() {
        return projection;
    }
    public void setProjection(String projection) {
        this.projection = projection == null ? null : projection.trim();
    }
    public String getBound() {
        return bound;
    }
    public void setBound(String bound) {
        this.bound = bound == null ? null : bound.trim();
    }
    public String getExtent() {
		return extent;
	}
	public void setExtent(String extent) {
		this.extent = extent;
	}
	public String getLayers() {
        return layers;
    }
    public void setLayers(String layers) {
        this.layers = layers == null ? null : layers.trim();
    }
    public String getTilesize() {
        return tilesize;
    }
    public void setTilesize(String tilesize) {
        this.tilesize = tilesize == null ? null : tilesize.trim();
    }
    public String getTileorigin() {
        return tileorigin;
    }
    public void setTileorigin(String tileorigin) {
        this.tileorigin = tileorigin == null ? null : tileorigin.trim();
    }
    public String getAngle() {
        return angle;
    }
    public void setAngle(String angle) {
        this.angle = angle == null ? null : angle.trim();
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
        this.remark = remark == null ? null : remark.trim();
    }
    public String getDirId() {
        return dirId;
    }
    public void setDirId(String dirId) {
        this.dirId = dirId;
    }
}