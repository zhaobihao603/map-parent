package com.imap.cloud.common.enums;

public enum Module {
	    LAYER("layer","地图图层"), 
	    SCENIC("scenic","校园风光"), 
	    USER("user","用户服务"), 
	    VR("vr","VR全景"), 
	    FEEDBACK("feedback","问题反馈"), 
	    ACTIVITY("activity","校园活动"), 
	    UNIT("unit","学校单位"), 
	    SYSTEM("system","系统服务"), 
	    TEACHER("teacher","师资力量"), 
	    GUIDE("guide","新生指引"), 
	    ROADBOOK("roadbook","路书服务"), 
	    ARBORETUM("arboretum","虚拟植物园"),
	    MISTAKE("mistake","纠错"),
	    PRESENTATION("presentation","解说");
	    // 成员变量  
	    private String code;
	    private String name;
	    // 构造方法  
	    private Module(String code,String name) {  
	        this.code = code; 
	        this.name = name;
	    }
	    
	    public String getCode() {
	        return code;
	    }
	    
	    public String getName() {
	        return name;
	    }

	/**
	 * 根据name获取去code
	 *
	 * @param name
	 * @return
	 */
	public static String getCodeByName(String name) {
		for (Module module : Module.values()) {
			if (name.equals(module.getName())) {
				return module.getCode();
			}
		}
		return null;
	}

	//覆盖方法
	    @Override  
	    public String toString() {  
	        return this.code+"_"+this.name;  
	    }  
}
