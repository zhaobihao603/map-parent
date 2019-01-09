package com.imap.cloud.common.entity.map;

public class MapLayerItem {
    private String id;

    private String name;

    private String address;

    private String alias;

    private String tags;

    private Double x;

    private Double y;
    
    private String geoType;
    
    private String description;

    private String xs;

    private String ys;

    private String bgcolor;

    private String color;

    private String lineweight;

    private String keywords;

    private MapLayer area;

    private MapLayer layer;

    private String imageId;

    private Boolean deleted;

    private String params;

    private String params1;

    private String params2;

    private String params3;

    private String params4;

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
        this.name = name == null ? null : name.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias == null ? null : alias.trim();
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags == null ? null : tags.trim();
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public String getGeoType() {
		return geoType;
	}

	public void setGeoType(String geoType) {
		this.geoType = geoType;
	}

	public void setY(Double y) {
        this.y = y;
    }
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getXs() {
        return xs;
    }

    public void setXs(String xs) {
        this.xs = xs == null ? null : xs.trim();
    }

    public String getYs() {
        return ys;
    }

    public void setYs(String ys) {
        this.ys = ys == null ? null : ys.trim();
    }

    public String getBgcolor() {
        return bgcolor;
    }

    public void setBgcolor(String bgcolor) {
        this.bgcolor = bgcolor == null ? null : bgcolor.trim();
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color == null ? null : color.trim();
    }

    public String getLineweight() {
        return lineweight;
    }

    public void setLineweight(String lineweight) {
        this.lineweight = lineweight == null ? null : lineweight.trim();
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords == null ? null : keywords.trim();
    }

    public MapLayer getArea() {
		return area;
	}
    public void setArea(MapLayer area) {
		this.area = area;
	}
    public MapLayer getLayer() {
		return layer;
	}
    public void setLayer(MapLayer layer) {
		this.layer = layer;
	}
    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params == null ? null : params.trim();
    }

    public String getParams1() {
        return params1;
    }

    public void setParams1(String params1) {
        this.params1 = params1 == null ? null : params1.trim();
    }

    public String getParams2() {
        return params2;
    }

    public void setParams2(String params2) {
        this.params2 = params2 == null ? null : params2.trim();
    }

    public String getParams3() {
        return params3;
    }

    public void setParams3(String params3) {
        this.params3 = params3 == null ? null : params3.trim();
    }

    public String getParams4() {
        return params4;
    }

    public void setParams4(String params4) {
        this.params4 = params4 == null ? null : params4.trim();
    }
}