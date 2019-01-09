/*
 * MapNode.java
 * Copyright(C) 20xx-2015 xxxxxx��˾
 * All rights reserved.
 * -----------------------------------------------
 * 2017-04-21 Created
 */
package com.imap.cloud.common.entity.map;

/**
 * 
 * 
 * @author ���ܴ���
 * @version 1.0 2017-04-21
 */
public class MapSchoolBorder {

    private String id;
    private String border;
    private String area;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }
    public String getBorder() {
        return border;
    }
    public void setBorder(String border) {
        this.border = border == null ? null : border.trim();
    }
    public String getArea() {
        return area;
    }
    public void setArea(String area) {
        this.area = area == null ? null : area.trim();
    }
}