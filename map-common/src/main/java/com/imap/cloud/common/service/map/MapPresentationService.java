package com.imap.cloud.common.service.map;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.annotations.Param;

import com.imap.cloud.common.entity.map.MapPresentation;
import com.imap.cloud.common.entity.map.MapPresentationExample;

public interface MapPresentationService {
	
	long countByExample(MapPresentationExample example);

	int deleteByExample(MapPresentationExample example);

	int deleteByPrimaryKey(String id);

	int insert(MapPresentation record);

	int insertSelective(MapPresentation record);
	List<MapPresentation> selectByExample(MapPresentationExample example);

	List<MapPresentation> selectByExampleForStartPage(MapPresentationExample example, Integer pageNum, Integer pageSize);

	List<MapPresentation> selectByExampleForOffsetPage(MapPresentationExample example, Integer offset, Integer limit);

	MapPresentation selectFirstByExample(MapPresentationExample example);

	MapPresentation selectByPrimaryKey(String id);

	int updateByExampleSelective(@Param("record") MapPresentation record, @Param("example") MapPresentationExample example);

	int updateByExample(@Param("record") MapPresentation record, @Param("example") MapPresentationExample example);

	int updateByPrimaryKeySelective(MapPresentation record);

	int updateByPrimaryKey(MapPresentation record);

	int deleteByPrimaryKeys(String ids);
	
	List<Map<String, Object>> searchAround(double distance, double x, double y);
	
	/**
	 * 获取最近的楼宇的记录
	 * @param x
	 * @param y
	 * @return
	 */
	Map<String, Object> getNearest(double x, double y);
	
	Map<String, Object> getById(String id);

	boolean saveWithFile(MapPresentation entity, HttpServletRequest request);
	
	public List<Map<String, Object>> getByExampleForStartPage(
			MapPresentationExample example, Integer pageNum, Integer pageSize);
}
