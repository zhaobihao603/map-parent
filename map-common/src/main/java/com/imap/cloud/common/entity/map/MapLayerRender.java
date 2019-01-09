package com.imap.cloud.common.entity.map;

import com.imap.cloud.common.entity.system.DictionaryItem;

public class MapLayerRender {
	private String id;
	private String layerId;
	private String showId;
	private String valueFields;
	private String renderStyle;
	private MapLayer layer;
	private DictionaryItem showType;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLayerId() {
		return layerId;
	}
	public void setLayerId(String layerId) {
		this.layerId = layerId;
	}
	public String getShowId() {
		return showId;
	}
	public void setShowId(String showId) {
		this.showId = showId;
	}
	public String getValueFields() {
		return valueFields;
	}
	public void setValueFields(String valueFields) {
		this.valueFields = valueFields;
	}
	public String getRenderStyle() {
		return renderStyle;
	}
	public void setRenderStyle(String renderStyle) {
		this.renderStyle = renderStyle;
	}
	public MapLayer getLayer() {
		return layer;
	}
	public void setLayer(MapLayer layer) {
		this.layer = layer;
	}
	public DictionaryItem getShowType() {
		return showType;
	}
	public void setShowType(DictionaryItem showType) {
		this.showType = showType;
	}
}
