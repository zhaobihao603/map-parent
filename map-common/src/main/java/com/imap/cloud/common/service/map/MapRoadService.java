package com.imap.cloud.common.service.map;

import java.util.List;
import java.util.Map;

import com.imap.cloud.common.dto.Pager;
import com.imap.cloud.common.entity.map.MapNode;
import com.imap.cloud.common.entity.map.MapPass;
import com.imap.cloud.common.entity.map.MapRoad;

public interface MapRoadService{
	/**
	  * 新增修改道路（路网基础走向、骨架）
	  * @param entity 
	  * @throws Exception
	  */
	void saveRoad(MapRoad entity) throws Exception;
	/**
	  * 修改通道属性，包括人行道、机动车道、自行车道、交通管制，单向道路走向（path）等设置
	  * @param entity 
	  * @throws Exception
	  */
	void updatePass(MapPass entity) throws Exception;
	
	/**
	  * 添加公交站等重要节点（未实现）
	  * @param entity 
	  * @throws Exception
	  */
	@Deprecated
	void savePoint(MapNode entity) throws Exception;
	/**
	  * 删除公交站等重要节点（未实现）
	  * @param entity 
	  * @throws Exception
	  */
	@Deprecated
	void deletePointByPk(String id) throws Exception;
	
	/**
	  * 根据对象主键删除指定道路
	  * @param id
	  * @return
	  * @throws Exception
	  */
	void deleteRoadByPk(String id) throws Exception;
	/**
	 * 根据校区删除道路，删除通道，删除邻接点集
	 * 当引入公交站概念时，需要考虑是否删除该校区的公交站
	 * @param areaId
	 * @throws Exception
	 */
	void deleteRoadByArea(String areaId) throws Exception;
	/**
	  * 删除通道,场景：设置单向道路（慎用）
	  * @param id
	  * @return
	  * @throws Exception
	  */
	void deletePass(String start,String end) throws Exception;
	/**
	  * 删除通道
	  * @param id
	  * @return
	  * @throws Exception
	  */
	public void deletePassById(String id) throws Exception;
	/**
	 * 根据校区道路网格，包括删除通道，删除邻接点集（该方法保留原始路网，便于重新构建通道和邻接点集）
	 * @param areaId
	 * @throws Exception
	 */
	void deleteGridByArea(String areaId) throws Exception;
	
	/**
	 * 分页获取道路列表
	 * @param entity
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	Pager<MapRoad> pageRoads(MapRoad entity, Integer pageNum, Integer pageSize);
	/**
	 * 根据主键获取道路 
	 * @param id
	 * @return
	 */
	MapRoad getRoad(String id);
	/**
	 * 根据校区id获取道路列表
	 * @param entity
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	List<MapRoad> getRoadsByArea(String areaId);
	/**
	 * 根据校区id构建道路网络
	 * @param areaId
	 */
	void buildInArea(String areaId) throws Exception;
	/**
	 * 根据校区id加载道路网络和邻接点
	 * @param areaId
	 */
	Map loadGridByArea(String areaId);
	/**
	 * 根据通道属性分页获取道路网络
	 * @param areaId
	 */
	Pager<MapRoad> pagePasses(MapPass entity, Integer pageNum, Integer pageSize);
	/**
	 * 根据主键获取通道 
	 * @param id
	 * @return
	 */
	MapPass getPassByPk(String id);
	/**
	 * 根据获取通道 
	 * @param id
	 * @return
	 */
	public MapPass getPassBy(String start, String end);
	/**
	 * 根据主键获取邻接点
	 * @param id
	 * @return
	 */
	MapNode getNodeById(String id);
	/**
	 * 框选拓扑查询
	 * @param area
	 * @param geometry
	 * @return
	 */
	List<MapPass> bufferQuery(String area, String geometry) throws Exception;
	
	public void multiSet(String[] arr, String footWay, String cycleWay,
			String driveWay, String trafficControl)throws Exception;
}
