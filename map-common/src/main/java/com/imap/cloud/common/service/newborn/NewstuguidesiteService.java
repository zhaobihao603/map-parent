package com.imap.cloud.common.service.newborn;

import com.imap.cloud.common.entity.newborn.Newstuguidesite;
import com.imap.cloud.common.service.base.BaseService;

public interface NewstuguidesiteService extends BaseService<Newstuguidesite,String> {
	
		int deleteByroute(String id);
	   
		int inserts(Newstuguidesite route);
		 
		int updateByPrimaryKey(Newstuguidesite record);
		 
		Newstuguidesite selectByPrimaryKey(String id);
		
		 int deleteByPrimaryKey(String id);
	
}
