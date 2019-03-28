package com.imap.cloud.common.dao.school;

import java.util.Map;

import com.imap.cloud.common.dao.base.BaseDao;
import com.imap.cloud.common.entity.school.TrafficGuideRoute;
import org.springframework.stereotype.Repository;

/**
 * 交通查询Dao
 * @author 冯林
 *
 */
@Repository
public interface TrafficGuideRouteMapper extends BaseDao<TrafficGuideRoute, String>{
	
	/**
	 * 根据起点和终点，进行交通查询
	 * @param reponse	
	 * @param startStation	起点		（数据字典Code）
	 * @param endStation	终点   	（数据字典Code）
	 * @param trafficType	乘车类型 	（数据字典Code）
	 * @param areaId		校区
	 * @return	TrafficGuideRoute
	 */
	TrafficGuideRoute selectTrafficQuery(Map<String, String> map);
	
	/**
	 * 删除该交通对应的节点数据
	 * @param id
	 */
	void deleteSite(String id);

}
