package com.imap.cloud.common.service.map;

import java.util.List;

import com.imap.cloud.common.dto.BaseLayer;
import com.imap.cloud.common.dto.Pager;
import com.imap.cloud.common.entity.map.MapLayer;
import com.imap.cloud.common.entity.map.MapSchoolBorder;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;

/**
 * 逻辑有点不一样，可以不需要继承BaseService
 * @author Administrator
 *
 */
public interface MapLayerService{
	public void insert(BaseLayer entity) throws Exception;
	public void insert(List<BaseLayer> list) throws Exception;
	public void deleteByPk(String[] id) throws Exception;
	public void deleteByPk(String id) throws Exception;
	public void updateByPk(BaseLayer entity) throws Exception;
	public BaseLayer selectByPk(String id) throws Exception;
	public List<BaseLayer> findAll() throws Exception;
	public Pager<BaseLayer> selectPage(BaseLayer entity, Integer pageNum, Integer pageSize)
			throws Exception;
	/**
	 * 插入或者更新
	 * @param entity
	 * @throws Exception
	 */
	public void save(MapLayer entity)throws Exception;
	public List<MapLayer> listByDirId(String id);
	public BaseLayer toDto(MapLayer entity);
	public MapLayer toEntity(BaseLayer dto);
	/**
	 * 根据alias获取配置图层
	 * @param baseLayer
	 * @return
	 */
	public List<MapLayer> getByAlias(String layerAlias);
	/**
	 * 根据地图底图图层
	 * @return
	 */
	public List<MapLayer> getBaseMap();
	/**
	 * 获取实体图层
	 * @return
	 */
	public List<MapLayer> getEntityLayer();
	/**
	 * 根据给予的点，逐一与各校区范围进行比较，得出所在校区id
	 * @param p
	 * @return
	 * @throws ParseException
	 */
	public String getAreaId(Point p) throws ParseException;
	/**
	 * 根据给予的点，逐一与各校区范围进行比较，得出所在校区
	 * @param p
	 * @return
	 * @throws ParseException
	 */
	public MapLayer getArea(Point p) throws ParseException;
	/**
	 * 把地图参数写入本地js
	 * @return
	 */
	public void writeInJS(String jsPath);
	/**
	 * 保存学校边界
	 * @param MapSchoolBorder
	 */
	public MapSchoolBorder saveSchoolBorder(MapSchoolBorder border);
	
	/**
	 * 根据主键删除学校边界
	 * @param MapSchoolBorder
	 */
	public void deleteSchoolBorder(String id);
	/**
	 * 根据学校areaId获取所有学校边界
	 * @param MapSchoolBorder
	 */
	public List<MapSchoolBorder> getSchoolBorderByArea(String areaId);
	
	/**
	 * 根据主键获取学校边界
	 * @param MapSchoolBorder
	 */
	public MapSchoolBorder getSchoolBorderById(String id);
	
	/**
	 * 获取学校边界
	 */
	public List<MapSchoolBorder> getSchoolBorder(MapSchoolBorder border);
}
