package com.imap.cloud.common.entity.system;

import java.util.Date;
import java.util.List;
import java.util.Vector;

public class SysLog {
    private String id;

    private String eventMethod;

    private String eventMethodDesc;

    private String eventObject;

    private String eventObjectDesc;

    private Date logTime;

    private String username;
    
    List<SysLogArgs> arguments;
    
    private String params; // 方法参数

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getEventMethod() {
        return eventMethod;
    }

    public void setEventMethod(String eventMethod) {
        this.eventMethod = eventMethod == null ? null : eventMethod.trim();
    }

    public String getEventMethodDesc() {
        return eventMethodDesc;
    }

    public void setEventMethodDesc(String eventMethodDesc) {
        this.eventMethodDesc = eventMethodDesc == null ? null : eventMethodDesc.trim();
    }

    public String getEventObject() {
        return eventObject;
    }

    public void setEventObject(String eventObject) {
        this.eventObject = eventObject == null ? null : eventObject.trim();
    }

    public String getEventObjectDesc() {
        return eventObjectDesc;
    }

    public void setEventObjectDesc(String eventObjectDesc) {
        this.eventObjectDesc = eventObjectDesc == null ? null : eventObjectDesc.trim();
    }

    public Date getLogTime() {
        return logTime;
    }

    public void setLogTime(Date logTime) {
        this.logTime = logTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

	public List<SysLogArgs> getArguments() {
		if (this.arguments == null) {
			this.arguments = new Vector();
		}
		return this.arguments;
	}

	public void setArguments(List<SysLogArgs> arguments) {
		this.arguments = arguments;
	}
	
	public boolean addArgument(SysLogArgs arg) {
		if (arg == null) {
			return false;
		}
		return getArguments().add(arg);
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}
}