package com.imap.cloud.common.dto;

public class WFSLayer extends BaseLayer {
	private String url;
	/**
     */
    private String featureNS;
    /**
     */
    private String featureType;
    /**
     */
    private String version;
    
    public WFSLayer(){
    	this.setLayerType("WFS");
    }    
    
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getFeatureNS() {
		return featureNS;
	}
	public void setFeatureNS(String featureNS) {
		this.featureNS = featureNS;
	}
	public String getFeatureType() {
		return featureType;
	}
	public void setFeatureType(String featureType) {
		this.featureType = featureType;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}

}
