package com.imap.cloud.common.service.lushu;

import com.imap.cloud.common.entity.lushu.Lushupath;
import com.imap.cloud.common.service.base.BaseService;


public interface LushupathService extends BaseService<Lushupath,String> {
	 
	Lushupath selectByPrimaryKey(String id);
	 
	 int deleteByPrimaryKey(String id);
	 
	 int insertSelective(Lushupath record);
	 
	 int updateByPrimaryKeySelective(Lushupath record);
}
