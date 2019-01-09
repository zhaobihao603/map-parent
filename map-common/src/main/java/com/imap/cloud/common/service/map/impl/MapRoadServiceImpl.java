package com.imap.cloud.common.service.map.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.geotools.geometry.jts.JTSFactoryFinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.util.StringUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.imap.cloud.common.aop.annotation.LogDescription;
import com.imap.cloud.common.dao.map.MapNodeMapper;
import com.imap.cloud.common.dao.map.MapPassMapper;
import com.imap.cloud.common.dao.map.MapRoadMapper;
import com.imap.cloud.common.dto.BaseLayer;
import com.imap.cloud.common.dto.Pager;
import com.imap.cloud.common.dto.WMSLayer;
import com.imap.cloud.common.entity.map.MapNode;
import com.imap.cloud.common.entity.map.MapPass;
import com.imap.cloud.common.entity.map.MapRoad;
import com.imap.cloud.common.service.map.MapLayerService;
import com.imap.cloud.common.service.map.MapRoadService;
import com.imap.cloud.common.util.AccountUtils;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.WKTReader;

@LogDescription(name="路网服务")
@Service
public class MapRoadServiceImpl implements MapRoadService {	
	private GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
	
	@Autowired
	private MapRoadMapper mapRoadMapper;
	@Autowired
	private MapNodeMapper mapNodeMapper;
	@Autowired
	private MapPassMapper mapPassMapper;
	@Autowired
	private MapLayerService mapLayerService;

	@Transactional
	@Override
	public void saveRoad(MapRoad entity) throws Exception {
		if(entity==null) throw new Exception("保存失败：对象为空!");
		if(StringUtils.isEmpty(entity.getId())){
			entity.setId(AccountUtils.getUUID());
			mapRoadMapper.insert(entity);
		}else
			mapRoadMapper.updateById(entity);
	}

	@Transactional
	@Override
	public void updatePass(MapPass entity) throws Exception {
		if(entity==null||(StringUtils.isEmpty(entity.getStart())&&StringUtils.isEmpty(entity.getEnd()))){
			throw new Exception("保存失败：对象为空或者未指定修改对象的主键!");
		}
		mapPassMapper.updateByPk(entity);
	}

	@Transactional
	@Override
	@Deprecated
	public void savePoint(MapNode entity) throws Exception {
		if(entity==null) throw new Exception("保存失败：无法保存空对象!");
		if(StringUtils.isEmpty(entity.getId())){
			entity.setId(AccountUtils.getUUID());
			mapNodeMapper.insert(entity);
		}else
			mapNodeMapper.updateById(entity);
	}
	
	@Transactional
	@Override
	@Deprecated
	public void deletePointByPk(String id) throws Exception{
		
	}

	@Transactional
	@Override
	public void deleteRoadByPk(String id) throws Exception {
		if(StringUtils.isEmpty(id)) throw new Exception("删除失败：主键为空");
		mapRoadMapper.deleteByPrimaryKey(id);
	}

	@Transactional
	@Override
	public void deleteRoadByArea(String areaId) throws Exception {
		int i = mapRoadMapper.deleteByArea(areaId);
		mapPassMapper.deleteByArea(areaId);
		mapNodeMapper.deleteByArea(areaId);
	}

	@Override
	@Transactional
	public void deletePass(String start, String end) throws Exception {
		if(StringUtils.isEmpty(start)||StringUtils.isEmpty(end))
			throw new Exception("删除失败：起点或终点主键为空！");
		mapPassMapper.deleteBy(start, end);
	}
	
	@Transactional
	@Override
	public void deletePassById(String id) throws Exception {
		if(StringUtils.isEmpty(id))
			throw new Exception("删除失败：主键为空！");
		mapPassMapper.deleteById(id);
	}
	
	@Transactional
	@Override
	public void deleteGridByArea(String areaId) throws Exception {
		if(StringUtils.isEmpty(areaId))
			throw new Exception("删除失败：请指定需要删除所有道路的校区主键！");
		mapPassMapper.deleteByArea(areaId);
		mapNodeMapper.deleteByArea(areaId);
	}

	@Override
	public Pager<MapRoad> pageRoads(MapRoad entity, Integer pageNum, Integer pageSize){
		Page<MapRoad> page = PageHelper.startPage(pageNum==null?1:pageNum,pageSize==null?10:pageSize,true);
        List<MapRoad> list = mapRoadMapper.getList(entity);
		return new Pager(list);
	}
	@Override
	public MapRoad getRoad(String id){
		if(StringUtils.isEmpty(id))return null;
		MapRoad road = mapRoadMapper.selectByPrimaryKey(id);
		return road;
	}
	@Override
	public List<MapRoad> getRoadsByArea(String areaId){
		MapRoad entity = new MapRoad();
		entity.setArea(areaId);
		List<MapRoad> list = mapRoadMapper.getList(entity);
		return list;
	}
	
	@Override
	@Transactional
	public void buildInArea(String areaId) throws Exception{
		MapRoad entity = new MapRoad();
		entity.setArea(areaId);
		WMSLayer bl = (WMSLayer)mapLayerService.selectByPk(areaId);
		int times = 1;
		if("EPSG:4490".equals(bl.getProjection())||"EPSG:4326".equals(bl.getProjection())){
			times *= 100000; 
		}
		List<MapRoad> list = mapRoadMapper.getList(entity);
		if(list==null || list.size()==0){
			throw new Exception("构建错误：该校区尚未绘画道路。");
		}
		MapRoad road = list.get(0);
		WKTReader reader = new WKTReader( geometryFactory );
		LineString line = null;
		Geometry geo = null;//最终的MULTILINESTRING
		geo = (LineString)reader.read(road.getPath());
		for(int j=1;j<list.size();j++){
			MapRoad next = list.get(j);
			line = (LineString)reader.read(next.getPath());
			geo = geo.union(line);
		}
		String geoJSON = geo.toString();
		String regex = "\\(([^()]*?)\\)";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(geoJSON);
		List<LineString> lines = new ArrayList<LineString>();
    	while(m.find()){
    		String matcher = m.group(0);
    		lines.add((LineString)reader.read("LINESTRING"+matcher));
    		geoJSON = geoJSON.replace(matcher, "");
    	}
    	MapNode node = null;
    	mapNodeMapper.deleteByArea(areaId);
    	mapPassMapper.deleteByArea(areaId);
    	Map<String,String> nodes = new HashMap<String,String>();
    	for(LineString subline:lines){
    		Coordinate[] coords = subline.getCoordinates();
    		
    		node = new MapNode();
    		node.setArea(areaId);
    		node.setName(null);
    		node.setX(coords[0].x);
    		node.setY(coords[0].y);
    		String startId = nodes.get(node.getX()+"#"+node.getY());
    		if(startId==null){
    			startId =AccountUtils.getUUID();
    			nodes.put(node.getX()+"#"+node.getY(), startId);
    			node.setId(startId);
        		mapNodeMapper.insert(node);//起点
    		}    		
    		
    		node.setX(coords[coords.length-1].x);
    		node.setY(coords[coords.length-1].y);
    		String endId = nodes.get(node.getX()+"#"+node.getY());
    		if(endId==null){
    			endId =AccountUtils.getUUID();
    			nodes.put(node.getX()+"#"+node.getY(), endId);
    			node.setId(endId);
        		mapNodeMapper.insert(node);//起点
    		}  
    		
    		MapPass pass = new MapPass();
    		pass.setId(AccountUtils.getUUID());
    		pass.setArea(areaId);
    		pass.setStart(startId);
    		pass.setEnd(endId);
    		pass.setFootWay("1");
    		pass.setDriveWay("1");
    		pass.setCycleWay("1");
    		pass.setTrafficControl("0");
    		pass.setLength(subline.getLength()*times);
    		pass.setPath(subline.toString());
    		mapPassMapper.insert(pass);//通道（正向）
    		
    		pass.setId(AccountUtils.getUUID());
    		pass.setArea(areaId);
    		pass.setStart(endId);
    		pass.setEnd(startId);
    		pass.setFootWay("1");
    		pass.setDriveWay("1");
    		pass.setCycleWay("1");
    		pass.setTrafficControl("0");
    		LineString reverse_line = (LineString)subline.reverse();
    		pass.setLength(reverse_line.getLength()*times);
    		pass.setPath(reverse_line.toString());
    		mapPassMapper.insert(pass);//通道（反向）
    	}
	}
	/**
	 * 如果该点还没添加进数据库，新增，否则直接返回数据库原有对象
	 * @param node
	 * @return
	 */
	@Transactional
	public MapNode addNode(MapNode node){
		MapNode entity = mapNodeMapper.getByXY(node.getX(),node.getY());
		if(entity!=null) return entity;
		String id =AccountUtils.getUUID();
		node.setId(id);
		mapNodeMapper.insert(node);//起点
		return node;
	}
	
	@Override
	public Map loadGridByArea(String areaId){
		Map<String,Object> map = new HashMap<String, Object>();
		List<MapNode> nodes = mapNodeMapper.getByAreaId(areaId);
		map.put("node", nodes);
		List<MapPass> passes = mapPassMapper.getByAreaId(areaId);
		map.put("pass", passes);
		return map;
	}
	
	public Pager<MapRoad> pagePasses(MapPass entity, Integer pageNum, Integer pageSize){
		Page<MapPass> page = PageHelper.startPage(pageNum==null?1:pageNum,pageSize==null?10:pageSize,true);
        List<MapPass> list = mapPassMapper.getList(entity);
		return new Pager(list);
	}
	
	public List<MapPass> bufferQuery(String areaId, String geometry) throws Exception{
		List<MapPass> passes = mapPassMapper.getByAreaId(areaId);
		WKTReader reader = new WKTReader( geometryFactory );
		Polygon polygon = (Polygon)reader.read(geometry);
		List<MapPass> list = new ArrayList<>();
		LineString line = null;
		for(MapPass m:passes){
			line = (LineString)reader.read(m.getPath());
			if(polygon.contains(line) || polygon.intersects(line))
				list.add(m);
		}
		return list;
	}
	
	public MapPass getPassByPk(String id){
		return mapPassMapper.selectByPrimaryKey(id);
	}
	
	public MapPass getPassBy(String start, String end){
		return mapPassMapper.getByStartEnd(start, end);
	}
	
	public MapNode getNodeById(String id){
		return mapNodeMapper.selectByPrimaryKey(id);
	}
	
	@Transactional
	public void multiSet(String[] arr, String footWay, String cycleWay,
			String driveWay, String trafficControl)throws Exception{
		MapPass pass = new MapPass();
		pass.setFootWay(footWay);
		pass.setCycleWay(cycleWay);
		pass.setDriveWay(driveWay);
		pass.setTrafficControl(trafficControl);
		for(int i=0;i<arr.length;i++){
			pass.setId(arr[i]);
			mapPassMapper.updateByPk(pass);
		}
	}

}
