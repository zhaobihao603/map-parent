package com.imap.cloud.common.service.map;


public interface MapEntityService{
	/**
	 * 从WFS上查询要素
	 * @return
	 */
	public String selectAllFromWFS();
	
	/**
	 * 获取中心点周边实体要素
	 * @param x
	 * @param y
	 * @param distance
	 * @return
	 */
	public String queryNearBy(Double x,Double y,Double distance);

	/**
	 * 根据名称和校区获取实体（弃用），改用query(String name, String type, String area, Integer max)
	 * @param name
	 * @param area
	 * @param max
	 * @return
	 */
	@Deprecated
	public String query(String name, String area, Integer max);
	
	/**
	 * 根据实体名称、校区名称和实体类型获取实体
	 * @param name 可为null
	 * @param type 可为null
	 * @param area 可为null
	 * @param max
	 * @return
	 */
	public String query(String name, String type, String area, Integer max);

}
