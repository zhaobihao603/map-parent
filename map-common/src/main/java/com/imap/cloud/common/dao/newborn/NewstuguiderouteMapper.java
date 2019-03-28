package com.imap.cloud.common.dao.newborn;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.imap.cloud.common.dao.base.BaseDao;
import com.imap.cloud.common.entity.newborn.Newstuguideroute;
import com.imap.cloud.common.entity.newborn.UnitDto;
import org.springframework.stereotype.Repository;

@Repository
public interface NewstuguiderouteMapper extends BaseDao<Newstuguideroute, String>  {
	
	 int deleteByPrimaryKey(String id);
	 
	 int inserts(Newstuguideroute route);
	 
	 int updateByPrimaryKey(Newstuguideroute record);
	 
	 List<Newstuguideroute> selectByPrimaryKey(String id);
	 
	 //发布
	 int updateByfabu(@Param(value="id") String id,@Param(value="fabu") boolean fabu);
	 
	 UnitDto selectNew(String id); 
     
}