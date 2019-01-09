package com.imap.cloud.common.service.school.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imap.cloud.common.aop.annotation.LogDescription;
import com.imap.cloud.common.dao.school.TrafficGuideRouteMapper;
import com.imap.cloud.common.entity.school.TrafficGuideRoute;
import com.imap.cloud.common.service.base.impl.BaseServiceImpl;
import com.imap.cloud.common.service.school.TrafficGuideRouteService;

@LogDescription(name="交通查询管理")
@Service("trafficGuideRouteServceImpl")
public class TrafficGuideRouteServceImpl extends BaseServiceImpl<TrafficGuideRoute, String> implements TrafficGuideRouteService{
	
	private TrafficGuideRouteMapper trafficGuideRouteMapper;
	
	@Autowired
	TrafficGuideRouteServceImpl(TrafficGuideRouteMapper trafficGuideRouteMapper){
		this.trafficGuideRouteMapper = trafficGuideRouteMapper;
		super.setBaseDao(trafficGuideRouteMapper);
	}

	@Override
	public TrafficGuideRoute selectTrafficQuery(String startStation,
			String endStation, String trafficType) {
		Map<String,String> map = new HashMap<>();
		map.put("startStation", startStation);
		map.put("endStation", endStation);
		map.put("trafficType", trafficType);
		return trafficGuideRouteMapper.selectTrafficQuery(map);
	}

	public void deleteByPkSite(String id) throws Exception {
		super.deleteByPk(id);
		trafficGuideRouteMapper.deleteSite(id);
	}
}
