package com.imap.cloud.common.service.map;

import com.vividsolutions.jts.io.ParseException;

public interface RoadPlanningService {
	/**
	 * 在未知校区下的路径规划计算
	 * 
	 * @param x1 起点x
	 * @param y1 起点y
	 * @param x2 终点x
	 * @param y2 终点y
	 * @param type  选择出行方式
	 * @return
	 * @throws ParseException
	 */
	public String caculate(Double x1,Double y1,Double x2,Double y2,String type) throws Exception;
	/**
	 * 同一区域下的路网
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public String caculate_samearea(Double x1,Double y1,Double x2,Double y2,String type) throws Exception;

}
