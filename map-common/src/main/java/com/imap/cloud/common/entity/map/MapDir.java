package com.imap.cloud.common.entity.map;

public class MapDir {
    private String id;

    private String name;

    private Integer seq;

    private String enabled;

    private MapDir parent;//父目录

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

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled == null ? null : enabled.trim();
    }

    public MapDir getParent() {
        return parent;
    }

    public void setParent(MapDir parent) {
        this.parent = parent;
    }
}