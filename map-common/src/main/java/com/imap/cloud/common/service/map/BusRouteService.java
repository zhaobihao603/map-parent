package com.imap.cloud.common.service.map;

import java.util.List;
import java.util.Map;

import com.imap.cloud.common.dto.Pager;
import com.imap.cloud.common.entity.map.BusRoute;

/**
 * 跨校区校巴service层接口
 * @author 99901745
 * @since 2017-04-10
 */
public interface BusRouteService {
	/**
	 * 查询校巴路线
	 * @param conditionMap
	 * @return
	 */
	public List<BusRoute> getBusRoutes(Map conditionMap);
	/**
	 * 分页查询校巴路线
	 * @param conditionMap
	 * @param pageSize
	 * @param pageNum
	 * @return
	 */
	public Pager<BusRoute> pageBusRoutes(Map conditionMap, Integer pageSize,
			Integer pageNum);
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
