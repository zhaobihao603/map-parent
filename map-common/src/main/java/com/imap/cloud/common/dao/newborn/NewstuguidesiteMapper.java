package com.imap.cloud.common.dao.newborn;

import java.util.List;

import com.imap.cloud.common.dao.base.BaseDao;
import com.imap.cloud.common.entity.newborn.Newstuguidesite;
import org.springframework.stereotype.Repository;

@Repository
public interface NewstuguidesiteMapper extends BaseDao<Newstuguidesite, String>  {
    
    int deleteByroute(String id);
   
    int deleteByPrimaryKey(String id);
	 
	int inserts(Newstuguidesite route);
	 
	int updateByPrimaryKey(Newstuguidesite record);
	 
	Newstuguidesite selectByPrimaryKey(String id);
	
	//查询节点集合
	List<Newstuguidesite> selectlist(String id );
    
}