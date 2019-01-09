package com.imap.cloud.common.enums;
/**
 * 出行方式
 * @author Administrator
 *
 */
public enum RouteType {
	    FOOT("foot","步行"), 
	    BICYCLE("bicycle","自行车"), 
	    AUTO("auto","汽车");
	    // 成员变量  
	    private String code;
	    private String name;
	    // 构造方法  
	    private RouteType(String code,String name) {  
	        this.code = code; 
	        this.name = name;
	    }
	    
	    public String getCode() {
	        return code;
	    }
	    
	    public String getName() {
	        return name;
	    }
	    //覆盖方法  
	    @Override  
	    public String toString() {  
	        return this.code+"_"+this.name;  
	    }  
}
