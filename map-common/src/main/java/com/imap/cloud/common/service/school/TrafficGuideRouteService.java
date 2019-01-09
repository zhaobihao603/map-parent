package com.imap.cloud.common.service.school;

import com.imap.cloud.common.entity.school.TrafficGuideRoute;
import com.imap.cloud.common.service.base.BaseService;

/**
 * 交通查询Service
 * @author 冯林
 *
 */
public interface TrafficGuideRouteService extends BaseService<TrafficGuideRoute, String> {
	
	/**
	 * 根据起点和终点，进行交通查询
	 * @param reponse	
	 * @param startStation	起点		
	 * @param endStation	终点	
	 * @param trafficType	乘车类型 	
	 * @return	TrafficGuideRoute
	 */
	TrafficGuideRoute selectTrafficQuery(String startStation, String endStation,
			String trafficType);
	
	/**
	 * 删除交通信息和对应的节点数据
	 * @param id
	 * @throws Exception 
	 */
	void deleteByPkSite(String id) throws Exception;

}
