package com.imap.cloud.common.dto;

public class WMSLayer extends BaseLayer {
	private String url;
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
     * 图层范围
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
    
    public WMSLayer(){
    	this.setLayerType("WMS");
    }
    
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getResolution() {
		return resolution;
	}
	public void setResolution(String resolution) {
		this.resolution = resolution;
	}
	public String getProjection() {
		return projection;
	}
	public void setProjection(String projection) {
		this.projection = projection;
	}
	public String getBound() {
		return bound;
	}
	public void setBound(String bound) {
		this.bound = bound;
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
		this.layers = layers;
	}
	public String getTilesize() {
		return tilesize;
	}
	public void setTilesize(String tilesize) {
		this.tilesize = tilesize;
	}
	public String getTileorigin() {
		return tileorigin;
	}
	public void setTileorigin(String tileorigin) {
		this.tileorigin = tileorigin;
	}

}
