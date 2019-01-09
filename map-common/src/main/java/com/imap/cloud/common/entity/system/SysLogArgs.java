package com.imap.cloud.common.entity.system;

public class SysLogArgs {
    private String id;

    private String classname;

    private String description;

    private String identifier;

    private String oper_uuid;

    private Integer argu_idx;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname == null ? null : classname.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier == null ? null : identifier.trim();
    }

    public String getOper_uuid() {
        return oper_uuid;
    }

    public void setOper_uuid(String oper_uuid) {
        this.oper_uuid = oper_uuid == null ? null : oper_uuid.trim();
    }

    public Integer getArgu_idx() {
        return argu_idx;
    }

    public void setArgu_idx(Integer argu_idx) {
        this.argu_idx = argu_idx;
    }
}