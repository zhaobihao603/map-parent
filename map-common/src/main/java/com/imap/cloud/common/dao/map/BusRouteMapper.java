package com.imap.cloud.common.dao.map;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.imap.cloud.common.entity.map.BusRoute;
/**
 * 跨校区校巴DAO层接口
 * @author 99901745
 * @since 2017-04-10
 */
public interface BusRouteMapper {
	/**
	 * 根据起始校区id查询校巴路线
	 * @param conditionMap
	 * @return
	 */
	public List<BusRoute> getBusRoutes(Map conditionMap);
	/**
	 * @param busRoute
	 * @return
	 */
	public int insert(BusRoute busRoute);
	/**
	 * @param busRoute
	 * @return
	 */
	public int update(BusRoute busRoute);
	/**
	 * @param id
	 * @return
	 */
	public int delete(String id);
}
