/*
 * MapNode.java
 * All rights reserved.
 * -----------------------------------------------
 * 2017-04-21 Created
 */
package com.imap.cloud.common.entity.map;

/**
 * 
 * 
 * @author
 * @version 1.0 2017-04-21
 */
public class MapNode {

    private String id;
    private String name;
    private Double x;
    private Double y;
    private String area;

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
    public Double getX() {
        return x;
    }
    public void setX(Double x) {
        this.x = x;
    }
    public Double getY() {
        return y;
    }
    public void setY(Double y) {
        this.y = y;
    }
    public String getArea() {
        return area;
    }
    public void setArea(String area) {
        this.area = area == null ? null : area.trim();
    }
}