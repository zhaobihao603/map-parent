package com.imap.cloud.common.service.map.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.geometry.jts.JTSFactoryFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.imap.cloud.common.aop.annotation.LogDescription;
import com.imap.cloud.common.aop.annotation.LogMethod;
import com.imap.cloud.common.dao.map.MapLayerMapper;
import com.imap.cloud.common.dao.map.MapSchoolBorderMapper;
import com.imap.cloud.common.dto.BaseLayer;
import com.imap.cloud.common.dto.CustomLayer;
import com.imap.cloud.common.dto.Pager;
import com.imap.cloud.common.dto.WFSLayer;
import com.imap.cloud.common.dto.WMSLayer;
import com.imap.cloud.common.entity.map.MapLayer;
import com.imap.cloud.common.entity.map.MapSchoolBorder;
import com.imap.cloud.common.listener.MapInitializationAfterStartupProcessor;
import com.imap.cloud.common.service.map.MapLayerService;
import com.imap.cloud.common.util.AccountUtils;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

@Service
public class MapLayerServiceImpl implements MapLayerService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
	private WKTReader reader = new WKTReader( geometryFactory );
	@Autowired private MapLayerMapper mapLayerMapper;
	@Autowired private MapSchoolBorderMapper mapSchoolBorderMapper;
	@Override
	public void insert(BaseLayer dto) throws Exception {
		MapLayer entity = toEntity(dto);
		entity.setAddDate(new Date());
		mapLayerMapper.insert(entity);
	}

	@Override
	public void insert(List<BaseLayer> list) throws Exception {
		for(BaseLayer dto:list){
			MapLayer entity = toEntity(dto);
			entity.setAddDate(new Date());
			mapLayerMapper.insert(entity);		
		}
	}
	
	@Override
	public void save(MapLayer entity) throws Exception {
		if(entity!=null && StringUtils.isEmpty(entity.getId())){
			entity.setAddDate(new Date());
			mapLayerMapper.insertSelective(entity);
		}else
			mapLayerMapper.updateByPkSelective(entity);
	}

	@Override
	public void deleteByPk(String[] ids) throws Exception {
		for(String id:ids){
			deleteByPk(id);
		}		
	}

	@Override
	public void deleteByPk(String id) throws Exception {
		mapLayerMapper.deleteByPk(id);
	}

	@Override
	public void updateByPk(BaseLayer dto) throws Exception {
		MapLayer entity = toEntity(dto);
		mapLayerMapper.updateByPk(entity);
	}

	@Override
	public BaseLayer selectByPk(String id) throws Exception {
		MapLayer entity = mapLayerMapper.selectByPk(id);
		BaseLayer dto = toDto(entity);
		return dto;
	}

	@Override
	public List<BaseLayer> findAll() throws Exception {
		List<MapLayer> li = mapLayerMapper.selectAll(null);
		List<BaseLayer> list = covertEntity2DTO(li);
		return list;
	}

	@LogMethod
	@LogDescription(name = "创建新的URL资源")
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Pager<BaseLayer> selectPage(BaseLayer dto, Integer pageNum, Integer pageSize)
			throws Exception {
		//PageHelper只对紧跟着的第一个SQL语句起作用
		Page<MapLayer> p = PageHelper.startPage(pageNum==null?1:pageNum,pageSize==null?10:pageSize,true);
		//pageList 其实是一个 包含结果的com.github.pagehelper.Page
		// pageList instanceof com.github.pagehelper.Page == true
        List<MapLayer> pageList = mapLayerMapper.selectAll(toEntity(dto));        
        
        List<BaseLayer> list = covertEntity2DTO(pageList);
		//可以用PageInfo对结果进行包装，PageInfo包装过度，可以用本项目的Pager
        Pager<BaseLayer> p2 = new Pager(list,p);
		return p2;
	}
	
	@Override
	public List<MapLayer> listByDirId(String dirId) {
		return mapLayerMapper.listByDirId(dirId);
	}
	
	/**
	 * 
	 * @param entity
	 * @return
	 */
	public BaseLayer covertEntity2DTO(MapLayer entity){
		return toDto(entity);
	}
	
	public List<BaseLayer> covertEntity2DTO(List<MapLayer> list){
		List<BaseLayer> dtos = new ArrayList<BaseLayer>();
		for(MapLayer entity:list){
			dtos.add(toDto(entity));
		}
		return dtos;
	}
	
	public MapLayer covertDTO2Entity(BaseLayer dto){
		return toEntity(dto);
	}
	
	public List<MapLayer> covertDTO2Entity(List<BaseLayer> list){
		List<MapLayer> entitys = new ArrayList<MapLayer>();
		for(BaseLayer dto:list){
			entitys.add(toEntity(dto));
		}
		return entitys;
	}
	
	public BaseLayer toDto(MapLayer entity){
		if(entity==null) return null;
		BaseLayer dto = null;
		if(entity.getLayerType()!=null && "wms".equalsIgnoreCase(entity.getLayerType().trim())){
			dto = new WMSLayer();
		}else if(entity.getLayerType()!=null && "wfs".equalsIgnoreCase(entity.getLayerType().trim())){
			dto = new WFSLayer();
		}else{
			dto = new CustomLayer();
		}
		BeanUtils.copyProperties(entity,dto);
		return dto;
	}
	
	public MapLayer toEntity(BaseLayer dto){
		if(dto==null) return null;
		MapLayer entity = new MapLayer();
		BeanUtils.copyProperties(dto,entity);
		return entity;
	}
	
	public List<MapLayer> getByAlias(String layerAlias){
		MapLayer entity = new MapLayer();
		entity.setAlias(layerAlias);
		entity.setDeleted(false);
		return mapLayerMapper.getBy(entity);
	}
	
	public List<MapLayer> getBaseMap(){
		MapLayer entity = new MapLayer();
		entity.setAlias("BASE_LAYER");
		entity.setDeleted(false);
		entity.setLayerType("WMS");
		return mapLayerMapper.getBy(entity);
	}
	
	public List<MapLayer> getEntityLayer(){
		MapLayer entity = new MapLayer();
		entity.setAlias("ENTITY_LAYER");
		entity.setDeleted(false);
		entity.setLayerType("WFS");
		return mapLayerMapper.getBy(entity);
	}
	
	/**
	 * 根据给予的点，逐一与各校区范围进行比较，得出所在校区id
	 * @param p
	 * @return
	 * @throws ParseException
	 */
	public String getAreaId(Point p) throws ParseException{
		List<MapLayer> layers = this.getBaseMap();
		String areaId = null;
		for(MapLayer l:layers){
			String[] xys = l.getRemark().split(",");
			StringBuffer sb = new StringBuffer("POLYGON((");
			for(int i=0;i<xys.length;i=i+2){
				sb.append(xys[i]);
				sb.append(" ");
				sb.append(xys[i+1]);
				sb.append(",");
			}				
			sb.append(xys[0]);
			sb.append(" ");
			sb.append(xys[1]).append("))");
			Polygon school = (Polygon)reader.read(sb.toString());
			
			if(school.contains(p)){
				areaId = l.getId();
				break;
			}
		}
		return areaId;
	}
	
	public MapLayer getArea(Point p) throws ParseException{
		List<MapLayer> layers = this.getBaseMap();
		for(MapLayer l:layers){
			String[] xys = l.getRemark().split(",");
			StringBuffer sb = new StringBuffer("POLYGON((");
			for(int i=0;i<xys.length;i=i+2){
				sb.append(xys[i]);
				sb.append(" ");
				sb.append(xys[i+1]);
				sb.append(",");
			}				
			sb.append(xys[0]);
			sb.append(" ");
			sb.append(xys[1]).append("))");
			Polygon school = (Polygon)reader.read(sb.toString());
			
			if(school.contains(p)){
				return l;
			}
		}
		return null;
	}
	
	public List<Map> getSchoolArea(){
		List<MapLayer> layers = this.getBaseMap();
		try {
			if(layers ==null || layers.size()==0){
				throw new Exception("未设置校区");
			}
			Map map = null;
			for(MapLayer layer:layers){
				map = new HashMap<String, Object>();
				map.put("id", layer.getId());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void writeInJS(String filePath){
		List<MapLayer> layers = this.getBaseMap();
		if(layers== null || layers.size()==0)
			return;
		List<MapLayer> entityLayers = this.getEntityLayer();
		MapLayer entity = null;
		if(entityLayers!=null && entityLayers.size()>0)entity = entityLayers.get(0);
		BufferedWriter writer = null;
        try {
        	if(StringUtils.isEmpty(filePath)){
        		String classPath = MapInitializationAfterStartupProcessor.class.getResource("/").getFile();
            	String path = classPath.substring(0, classPath.indexOf("WEB-INF"));
            	filePath = new StringBuffer(path).append("resource/imap/js/map.setting.v2.js").toString();
        	}        	
        	File file = new File(filePath);
        	if(!file.exists()) file.createNewFile();
        	OutputStreamWriter writerStream = new OutputStreamWriter(new FileOutputStream(filePath),"UTF-8");
            writer = new BufferedWriter(writerStream);
            
            writer.write("var basemap_wms_param = ");
            writer.write(JSON.toJSONString(layers).replaceAll(" ", "").replace("\r\r\n","").replace("\t",""));
            writer.write(";");
            writer.newLine();//换行
            if(entity!=null){
	            writer.write("var entity_wfs_param = {url:\""+entity.getUrl());
	            writer.write("\",featureNS:\""+entity.getFeatureNS());
	            String featureType = entity.getFeatureType();
	            writer.write("\",featureType:\""+featureType.substring(featureType.indexOf(":")+1)+"\"};");
            }else
            	writer.write("var entity_wfs_param = {url:\"\",featureNS:\"\",featureType:\"\"};");
            writer.newLine();//换行
            writer.write("var config = new IMAP.Config({scale : 1,overlook : 30, rotate : 0});");
            writer.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
	}
	
	@Override
	public MapSchoolBorder saveSchoolBorder(MapSchoolBorder border){
		try {
			if(border==null)return null;
			if(StringUtils.isEmpty(border.getBorder())){
				throw new Exception("boder can not be empty");
			}
			if(StringUtils.isEmpty(border.getArea())){
				throw new Exception("area can not be empty");
			}
			MapLayer layer = mapLayerMapper.selectByPk(border.getArea());
			if(layer==null || !"BASE_LAYER".equals(layer.getAlias()))
				throw new Exception("area is illegal");
				
			if(StringUtils.isEmpty(border.getId())){
				border.setId(AccountUtils.getUUID());
				mapSchoolBorderMapper.insert(border);
			}else
				mapSchoolBorderMapper.updateById(border);
			return border;
        } catch (Exception e) {
            e.printStackTrace();
        }
		return null;
	}
	
	@Override
	public void deleteSchoolBorder(String id){
		try {
			if(StringUtils.isEmpty(id)){
				throw new Exception("id can not be empty");
			}
			mapSchoolBorderMapper.deleteByPrimaryKey(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	@Override
	public List<MapSchoolBorder> getSchoolBorderByArea(String areaId){
		try {
			if(StringUtils.isEmpty(areaId))
				throw new Exception("areaId can not be empty");
				
			return mapSchoolBorderMapper.getByAreaId(areaId);
        } catch (Exception e) {
            e.printStackTrace();
        }
		return null;
	}
	
	@Override
	public MapSchoolBorder getSchoolBorderById(String id){
		try {
			if(StringUtils.isEmpty(id))
				throw new Exception("id can not be empty");
				
			return mapSchoolBorderMapper.selectByPrimaryKey(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
		return null;
	}
	
	@Override
	public List<MapSchoolBorder> getSchoolBorder(MapSchoolBorder border){
		try {
			return mapSchoolBorderMapper.selectAll(border);
        } catch (Exception e) {
            e.printStackTrace();
        }
		return null;
	}

}
