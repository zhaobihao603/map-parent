package com.imap.cloud.common.entity.map;

import com.imap.cloud.common.entity.system.DictionaryItem;

public class BusRoute {
	private String id;
	private String name;
	private String fromId;
	private String toId;
	private String xs;
	private String ys;
	private DictionaryItem from;
	private DictionaryItem to;
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
	public String getFromId() {
		return fromId;
	}
	public void setFromId(String fromId) {
		this.fromId = fromId;
	}
	public String getToId() {
		return toId;
	}
	public void setToId(String toId) {
		this.toId = toId;
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
	public DictionaryItem getFrom() {
		return from;
	}
	public void setFrom(DictionaryItem from) {
		this.from = from;
	}
	public DictionaryItem getTo() {
		return to;
	}
	public void setTo(DictionaryItem to) {
		this.to = to;
	}
	@Override
	public String toString() {
		return "BusRoute [id=" + id + ", name=" + name + ", fromId=" + fromId
				+ ", toId=" + toId + ", xs=" + xs + ", ys=" + ys + ", from="
				+ from + ", to=" + to + "]";
	}
	
}
