package com.imap.cloud.common.service.school.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imap.cloud.common.aop.annotation.LogDescription;
import com.imap.cloud.common.dao.school.TrafficGuideSiteMapper;
import com.imap.cloud.common.entity.school.TrafficGuideSite;
import com.imap.cloud.common.service.base.impl.BaseServiceImpl;
import com.imap.cloud.common.service.school.TrafficGuideSiteService;

@LogDescription(name="交通查询节点管理")
@Service("trafficGuideSiteServceImpl")
public class TrafficGuideSiteServceImpl extends BaseServiceImpl<TrafficGuideSite, String> implements TrafficGuideSiteService{
	
	@SuppressWarnings("unused")
	private TrafficGuideSiteMapper trafficGuideSiteMapper;
	
	@Autowired
	TrafficGuideSiteServceImpl(TrafficGuideSiteMapper trafficGuideSiteMapper){
		this.trafficGuideSiteMapper = trafficGuideSiteMapper;
		super.setBaseDao(trafficGuideSiteMapper);
	}
}
