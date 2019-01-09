package com.imap.cloud.common.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.imap.cloud.common.service.map.MapLayerService;

public class MapInitializationAfterStartupProcessor implements ApplicationListener<ContextRefreshedEvent>{
	
	@Autowired public MapLayerService mapLayerService;
	/**
	* 当一个ApplicationContext被初始化或刷新触发
	*/
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		//需要执行的逻辑代码，当spring容器初始化完成后就会执行该方法。
		if(event.getApplicationContext().getDisplayName().equals("Root WebApplicationContext")){
			mapLayerService.writeInJS(null);
		}
	}
}
