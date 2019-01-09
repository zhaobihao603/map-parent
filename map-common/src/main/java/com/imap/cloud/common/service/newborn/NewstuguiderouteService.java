package com.imap.cloud.common.service.newborn;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.imap.cloud.common.entity.newborn.Newstuguideroute;
import com.imap.cloud.common.entity.newborn.Newstuguidesite;
import com.imap.cloud.common.entity.newborn.UnitDto;
import com.imap.cloud.common.service.base.BaseService;

public interface NewstuguiderouteService extends BaseService<Newstuguideroute,String> {
	
	 int deleteByPrimaryKey(String id);
	 
	 int inserts(Newstuguideroute route);
	 
	 int updateByPrimaryKey(Newstuguideroute record);
	 
	 List<Newstuguideroute> selectByPrimaryKey(String id);
	 
	 int updateByfabu(@Param(value="id")String id,@Param(value="fabu") boolean fabu);
	 
	 List<Newstuguidesite> selectlist(String id );
	 
	 /**
	  * 查询新生指引详情(只用于前端)
	  * @param id
	  * @return
	  */
	 UnitDto selectNew(String id); 
}
