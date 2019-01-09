package com.imap.cloud.common.service.map.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.geotools.geometry.jts.JTSFactoryFinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imap.cloud.common.aop.annotation.LogDescription;
import com.imap.cloud.common.dao.map.MapNodeMapper;
import com.imap.cloud.common.dao.map.MapPassMapper;
import com.imap.cloud.common.dto.road.Graphic;
import com.imap.cloud.common.dto.road.Vertex;
import com.imap.cloud.common.entity.map.BusRoute;
import com.imap.cloud.common.entity.map.MapLayer;
import com.imap.cloud.common.entity.map.MapPass;
import com.imap.cloud.common.enums.RouteType;
import com.imap.cloud.common.service.map.BusRouteService;
import com.imap.cloud.common.service.map.MapLayerService;
import com.imap.cloud.common.service.map.MapTransformService;
import com.imap.cloud.common.service.map.RoadPlanningService;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

@LogDescription(name="路径规划的服务")
@Service
public class RoadPlanningServiceImpl implements RoadPlanningService{
	private GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
	private WKTReader reader = new WKTReader( geometryFactory );
	@Autowired private MapLayerService mapLayerService;
	@Autowired private BusRouteService busRouteService;
	@Autowired private MapTransformService mapTransformService;
	@Autowired private MapPassMapper mapPassMapper;
	@Autowired private MapNodeMapper mapNodeMapper;
	@Autowired private MapNodeMapper mapRoadMapper;
	
	public String caculate_samearea(Double x1,Double y1,Double x2,Double y2,String type) throws Exception{
		//如果少于100米，直接返回两点一线
		if(x1==null || y1 ==null ||x2==null||y2==null
				||x1==0 || y1 ==0 ||x2==0||y2==0) throw new Exception("{\"success\":false,\"message\":\"起点或终点坐标格式错误.\"}");
		Point start_point = (Point) reader.read(new StringBuffer("POINT(").append(x1).append(" ")
					.append(y1).append(")").toString());
		Point dest_point = (Point) reader.read(new StringBuffer("POINT(").append(x2).append(" ")
				.append(y2).append(")").toString());
		type = RouteType.BICYCLE.getCode().equals(type)
				||RouteType.AUTO.getCode().equals(type)?type:RouteType.FOOT.getCode();
		
		MapPass entity = new MapPass();
		if(RouteType.FOOT.getCode().equals(type))
			entity.setFootWay("1");
		if(RouteType.BICYCLE.getCode().equals(type))
			entity.setCycleWay("1");
		if(RouteType.AUTO.getCode().equals(type))
			entity.setDriveWay("1");
		entity.setTrafficControl("0");
		List<MapPass> passes = mapPassMapper.getList(entity);
		if(passes==null ||passes.size()==0)
			throw new Exception("{\"success\":false,\"message\":\"该校区尚未划定路网.\"}");
		//构建邻接矩阵,并分别找出里起点终点最近的通道
		Graphic g = new Graphic();
		Double away_start_shortest_distanc = Double.MAX_VALUE;
		Double away_dest_shortest_distanc = Double.MAX_VALUE;
		MapPass start_pass = null;
		MapPass dest_pass = null;
		
		for(int i=0;i<passes.size();i++){
			MapPass p = passes.get(i);
			LineString line = (LineString) reader.read(p.getPath());
			Double d1 = line.distance(start_point);
			Double d2 = line.distance(dest_point);	
			if(d1<away_start_shortest_distanc){
				away_start_shortest_distanc = d1;//离起点最近
				start_pass = p;
			}
			if(d2<away_dest_shortest_distanc){
				away_dest_shortest_distanc = d2;
				dest_pass = p;
			}
			g.addVertex(p.getStart(),new Vertex(p.getEnd(), p.getLength()));
		}
		
		//最近出发点的两条道路（也可能是单向车道）
		List<MapPass> from_twoWays =mapPassMapper.get2Way(start_pass.getStart(),start_pass.getEnd());
		for(MapPass pass:from_twoWays){
			//计算起点到最近道路的点(待完善，如果要考虑单向)
			List<MapPass> list = this.intersection(start_point, pass, false);//起点到附近最近的道路
			if(list!=null){
				passes.addAll(list);
				for(MapPass mapPass:list){
					System.out.println(mapPass.toString());
					g.addVertex(mapPass.getStart(),new Vertex(mapPass.getEnd(), mapPass.getLength()));
				}
			}
		}		
		
		List<MapPass> to_twoWays =mapPassMapper.get2Way(dest_pass.getStart(),dest_pass.getEnd());
		for(MapPass pass:to_twoWays){
			//计算终点最近道路到终点(要考虑单向车道)
			List<MapPass> list = this.intersection(dest_point, pass, true);//附近最近的道路到终点
			if(list!=null){
				passes.addAll(list);
				for(MapPass mapPass:list){
					System.out.println(mapPass.toString());
					g.addVertex(mapPass.getStart(),new Vertex(mapPass.getEnd(), mapPass.getLength()));
				}
			}
		}
		Vertex dest = null;
		g.addVertex(x2+","+y2,dest);//确保终点也放到vertexs里
		
		//计算
		List<String> routes = g.getShortestPath(x1+","+y1, x2+","+y2);//["E","H","C","B"]
		StringBuffer result = new StringBuffer("{\"routes\":[");
		Double total = 0d;
		//拼接为前台接收的格式
		for(int i =1; i<routes.size(); i++){
			String p = routes.get(i-1);
			String next = routes.get(i);			
			result.append("{");
			for(int j=0;j<passes.size();j++){
				MapPass pass = passes.get(j);
				if(p.equals(pass.getStart()) && next.equals(pass.getEnd())){
					result.append("\"route\":\"");
					result.append(pass.getPath());
					result.append("\",\"desc\":\"");
					result.append("\",\"startId\":\"");
					result.append(pass.getStart());
					result.append("\",\"endId\":\"");
					result.append(pass.getEnd());
					result.append("\",\"length\":");
					total += pass.getLength();
					result.append(pass.getLength());
					break;
				}
			}
			result.append("},");
		}
		result.deleteCharAt(result.lastIndexOf(","));
		result.append("],\"totalDistance\":"+total+"}");
		return result.toString();
	}
	
	
	
	public String caculateBAK20171109(Double x1,Double y1,Double x2,Double y2,String type) throws Exception{
		//如果少于100米，直接返回两点一线
		if(x1==null || y1 ==null ||x2==null||y2==null
				||x1==0 || y1 ==0 ||x2==0||y2==0) throw new Exception("{\"success\":false,\"message\":\"起点或终点坐标格式错误.\"}");
		Point start_point = (Point) reader.read(new StringBuffer("POINT(").append(x1).append(" ")
					.append(y1).append(")").toString());
		Point dest_point = (Point) reader.read(new StringBuffer("POINT(").append(x2).append(" ")
				.append(y2).append(")").toString());
		type = RouteType.BICYCLE.getCode().equals(type)
				||RouteType.AUTO.getCode().equals(type)?type:RouteType.FOOT.getCode();
		
		MapLayer startSchool = mapLayerService.getArea(start_point);
		MapLayer destSchool = mapLayerService.getArea(dest_point);
		boolean start_iswgs = false;
		if("EPSG:4490".equals(startSchool.getProjection())||"EPSG:4326".equals(startSchool.getProjection())){
			start_iswgs = true;
		}
		boolean dest_iswgs = false;
		if("EPSG:4490".equals(destSchool.getProjection())||"EPSG:4326".equals(destSchool.getProjection())){
			dest_iswgs = true;
		}
		List<MapPass> passes = null;
		boolean different = false;//不同校
		Point boarding = null;//上车点
		Point breakout = null;//下车点
		if(startSchool.getId().equals(destSchool.getId())){//同一校区
			passes = mapPassMapper.getAvailableByAreaId(type,startSchool.getId());
		}else{//跨校区
			different = true;
			//起点到乘车点
			passes = mapPassMapper.getAvailableByAreaId(type,startSchool.getId());
			
			//A校区到B校区
			Map<String,String> map = new HashMap<String,String>();
			map.put("fromId", startSchool.getId());
			map.put("toId", destSchool.getId());
			List<BusRoute> list = busRouteService.getBusRoutes(map);
			if(list == null || list.size()==0) throw new Exception("{\"success\":false,\"message\":\"跨校区，无校巴相连.\"}");
			if(list!=null && list.size()>0){
				BusRoute busRoute = list.get(0);
				String[] xs = busRoute.getXs().split(",");
				String[] ys = busRoute.getYs().split(",");
				StringBuffer path= new StringBuffer("LINESTRING(");
				for(int i=0;i<xs.length;i++){
					if (i >0) {
						path.append(",");
					}
					path.append(xs[i]+" "+ys[i]);
				}
				path.append(")");
				double[] bus_start = mapTransformService.wgs842utm(Double.valueOf(xs[0]), Double.valueOf(ys[0]));
				boarding = geometryFactory.createPoint(new Coordinate(bus_start[0],bus_start[1]));
				double[] bus_dest = mapTransformService.wgs842utm(Double.valueOf(xs[xs.length-1]), Double.valueOf(ys[ys.length-1]));
				breakout = geometryFactory.createPoint(new Coordinate(bus_dest[0],bus_dest[1]));
				MapPass e = new MapPass();
				e.setStart(bus_start[0]+","+bus_start[1]);
				e.setEnd(bus_dest[0]+","+bus_dest[1]);
				e.setPath(path.toString());
				e.setDriveWay("imap");
				Geometry busline = (Geometry)reader.read(path.toString());
				e.setLength(busline.getLength()*100000);//WGS84坐标
				passes.add(e);
			}
			//下车点到终点
			passes.addAll(mapPassMapper.getAvailableByAreaId(type,destSchool.getId()));
		}
		if(passes==null ||passes.size()==0)
			throw new Exception("{\"success\":false,\"message\":\"该校区尚未划定路网.\"}");
		//构建邻接矩阵,并分别找出里起点终点最近的通道
		Graphic g = new Graphic();
		Double away_start_shortest_distanc = Double.MAX_VALUE;
		Double away_dest_shortest_distanc = Double.MAX_VALUE;
		MapPass start_pass = null;
		MapPass dest_pass = null;
		
		Double away_boarding_shortest = Double.MAX_VALUE;
		Double away_breakout_shortest = Double.MAX_VALUE;
		MapPass boarding_pass = null;
		MapPass breakout_pass = null;
		for(int i=0;i<passes.size();i++){
			MapPass p = passes.get(i);
			LineString line = (LineString) reader.read(p.getPath());
			Double d1 = line.distance(start_point);
			Double d2 = line.distance(dest_point);	
			if(d1<away_start_shortest_distanc){
				away_start_shortest_distanc = d1;//离起点最近
				start_pass = p;
			}
			if(d2<away_dest_shortest_distanc){
				away_dest_shortest_distanc = d2;
				dest_pass = p;
			}
			if(different){//不同校区
				Double d3 = line.distance(boarding);
				Double d4 = line.distance(breakout);
				if(d3<away_boarding_shortest){
					away_boarding_shortest = d3;//离上车点最近
					boarding_pass = p;
				}
				if(d4<away_breakout_shortest){
					away_breakout_shortest = d4;//离下车点最近
					breakout_pass = p;
				}
			}
			g.addVertex(p.getStart(),new Vertex(p.getEnd(), p.getLength()));
		}
		
		List<MapPass> ways =mapPassMapper.get2Way(start_pass.getStart(),start_pass.getEnd());
		for(MapPass p:ways){
			LineString lineString = (LineString) reader.read(p.getPath());
			Map<String, Object> detail= distanceTo(start_point, lineString, true);
			double x0 = (double)detail.get("x");
			double y0 = (double)detail.get("y");
			LineString l1 =geometryFactory.createLineString(new Coordinate[]{new Coordinate(x1, y1),new Coordinate(x0,y0)});
			if(!l1.intersects(lineString)){
				double y = (0-x0)*(y1-y0)/(x1-x0)+y0;//与y轴相交的点（x,y）=> x=0;
				l1 = geometryFactory.createLineString(new Coordinate[]{new Coordinate(x1, y1),new Coordinate(0,y)});
			}
			if(!l1.intersects(lineString)){
				double x= (0-y0)*(x1-x0)/(y1-y0)+x0;//与x轴相交的点（x,y）=> y=0;
				l1 = geometryFactory.createLineString(new Coordinate[]{new Coordinate(x1, y1),new Coordinate(x,0)});
			}
			if(!l1.intersects(lineString)){
				throw new Exception("{\"success\":false,\"message\":\"l1无法找到交点.\"}");
			}
			MultiLineString lines = (MultiLineString)l1.union(lineString);
			if(start_pass.getPath().equals(dest_pass.getPath())){
				detail= distanceTo(dest_point, lineString, true);
				x0 = (double)detail.get("x");
				y0 = (double)detail.get("y");
				LineString l2 =geometryFactory.createLineString(new Coordinate[]{new Coordinate(x0,y0),new Coordinate(x2, y2)});
				if(!l2.intersects(lineString)){
					double y = (0-x0)*(y2-y0)/(x2-x0)+y0;//与y轴相交的点（x,y）=> x=0;
					l2 = geometryFactory.createLineString(new Coordinate[]{new Coordinate(0,y),new Coordinate(x2, y2)});
				}
				if(!l2.intersects(lineString)){
					double x= (0-y0)*(x2-x0)/(y2-y0)+x0;//与x轴相交的点（x,y）=> y=0;
					l2 = geometryFactory.createLineString(new Coordinate[]{new Coordinate(x,0),new Coordinate(x2, y2)});
				}
				if(!l2.intersects(lineString)){
					throw new Exception("{\"success\":false,\"message\":\"l2无法找到交点.\"}");
				}
				lines = (MultiLineString)l1.union(lineString).union(l2);
			}
			int n = lines.getNumGeometries();
			for(int i =0;i<n;i++){
				LineString line = (LineString)lines.getGeometryN(i);
				MapPass pass_m = new MapPass();
				pass_m.setFootWay("1");
				pass_m.setDriveWay("0");
				pass_m.setCycleWay("0");
				pass_m.setStart(line.getStartPoint().getX()==lineString.getStartPoint().getX()&&line.getStartPoint().getY()==lineString.getStartPoint().getY()?
						p.getStart():(line.getStartPoint().getX()==lineString.getEndPoint().getX()&&line.getStartPoint().getY()==lineString.getEndPoint().getY()?
								p.getEnd():line.getStartPoint().getX()+","+line.getStartPoint().getY()));				
				pass_m.setEnd(line.getEndPoint().getX()==lineString.getStartPoint().getX()&&line.getEndPoint().getY()==lineString.getStartPoint().getY()?
						p.getStart():(line.getEndPoint().getX()==lineString.getEndPoint().getX()&&line.getEndPoint().getY()==lineString.getEndPoint().getY()?
								p.getEnd():line.getEndPoint().getX()+","+line.getEndPoint().getY()));
				pass_m.setLength(line.getLength());
				pass_m.setPath(line.toString());
				passes.add(pass_m);
				g.addVertex(pass_m.getStart(),new Vertex(pass_m.getEnd(), pass_m.getLength()));
			}
		}
		if(!start_pass.getId().equals(dest_pass.getId())){
			List<MapPass> ways2 = mapPassMapper.get2Way(dest_pass.getStart(),dest_pass.getEnd());
			for(MapPass p:ways2){			
				LineString lineString = (LineString) reader.read(p.getPath());
				Map<String, Object> detail= distanceTo(dest_point, lineString, true);
				double x0 = (double)detail.get("x");
				double y0 = (double)detail.get("y");
				LineString l2 =geometryFactory.createLineString(new Coordinate[]{new Coordinate(x0,y0),new Coordinate(x2, y2)});
				if(!l2.intersects(lineString)){
					double y = (0-x0)*(y2-y0)/(x2-x0)+y0;//与y轴相交的点（x,y）=> x=0;
					l2 = geometryFactory.createLineString(new Coordinate[]{new Coordinate(0,y),new Coordinate(x2, y2)});
				}
				if(!l2.intersects(lineString)){
					double x= (0-y0)*(x2-x0)/(y2-y0)+x0;//与x轴相交的点（x,y）=> y=0;
					l2 = geometryFactory.createLineString(new Coordinate[]{new Coordinate(x,0),new Coordinate(x2, y2)});
				}
				if(!l2.intersects(lineString)){
					throw new Exception("{\"success\":false,\"message\":\"l2无法找到交点.\"}");
				}
				Point inter = (Point)l2.intersection(lineString);
				MultiLineString lines = (MultiLineString)l2.union(lineString);
				int n = lines.getNumGeometries();
				for(int i =0;i<n;i++){
					LineString line = (LineString)lines.getGeometryN(i);
					if(line.getEndPoint().getX() ==x2 
							|| line.getStartPoint().getX()==lineString.getStartPoint().getX()){
						MapPass pass_m = new MapPass();
						pass_m.setFootWay("1");
						pass_m.setDriveWay("0");
						pass_m.setCycleWay("0");
						pass_m.setStart(line.getStartPoint().getX()==lineString.getStartPoint().getX()&&line.getStartPoint().getY()==lineString.getStartPoint().getY()?
								p.getStart():(line.getStartPoint().getX()==lineString.getEndPoint().getX()&&line.getStartPoint().getY()==lineString.getEndPoint().getY()?
										p.getEnd():line.getStartPoint().getX()+","+line.getStartPoint().getY()));				
						pass_m.setEnd(line.getEndPoint().getX()==lineString.getStartPoint().getX()&&line.getEndPoint().getY()==lineString.getStartPoint().getY()?
								p.getStart():(line.getEndPoint().getX()==lineString.getEndPoint().getX()&&line.getEndPoint().getY()==lineString.getEndPoint().getY()?
										p.getEnd():line.getEndPoint().getX()+","+line.getEndPoint().getY()));
						pass_m.setLength(line.getLength());
						pass_m.setPath(line.toString());
						passes.add(pass_m);
						g.addVertex(pass_m.getStart(),new Vertex(pass_m.getEnd(), pass_m.getLength()));
					}
				}
			}
		}
		
		Vertex dest = null;
		g.addVertex(x2+","+y2,dest);//确保终点也放到vertexs里
		
		if(boarding_pass != null &&	breakout_pass !=  null){
			List<MapPass> boarding_twoWays =mapPassMapper.get2Way(boarding_pass.getStart(),boarding_pass.getEnd());
			for(MapPass pass:boarding_twoWays){
				List<MapPass> list = this.intersection(boarding, pass, true);//附近最近的道路到上车点
				if(list!=null){
					passes.addAll(list);
					for(MapPass mapPass:list){
						g.addVertex(mapPass.getStart(),new Vertex(mapPass.getEnd(), mapPass.getLength()));
					}
				}
			}		
			
			List<MapPass> breakout_twoWays =mapPassMapper.get2Way(breakout_pass.getStart(),breakout_pass.getEnd());
			for(MapPass pass:breakout_twoWays){
				List<MapPass> list = this.intersection(breakout, pass, false);//下车点到附近最近的道路
				if(list!=null){
					passes.addAll(list);
					for(MapPass mapPass:list){
						g.addVertex(mapPass.getStart(),new Vertex(mapPass.getEnd(), mapPass.getLength()));
					}
				}
			}
			g.addVertex(breakout.getX()+","+breakout.getY(),dest);//确保下车点也放到vertexs里
		}
		//计算
		List<String> routes = g.getShortestPath(x1+","+y1, x2+","+y2);//["E","H","C","B"]
		StringBuffer result = new StringBuffer("{\"routes\":[");
		Double total = 0d;
		boolean iswgs = start_iswgs;
		//拼接为前台接收的格式
		for(int i =1; i<routes.size(); i++){
			String p = routes.get(i-1);
			String next = routes.get(i);			
			result.append("{");
			for(int j=0;j<passes.size();j++){
				MapPass pass = passes.get(j);
				if(p.equals(pass.getStart()) && next.equals(pass.getEnd())){
					result.append("\"route\":\"");
					//跨校区时，非WGS84坐标的路段全部转为WGS84格式的坐标
					result.append(different &&  !iswgs && !pass.getDriveWay().equals("imap")?
							this.transform(pass.getPath()).toString():pass.getPath());
					result.append("\",\"desc\":\"");
					result.append("\",\"startId\":\"");
					result.append(pass.getStart());
					result.append("\",\"endId\":\"");
					result.append(pass.getEnd());
					result.append("\",\"length\":");
					total += pass.getLength();
					result.append(pass.getLength());
					if(different && pass.getDriveWay().equals("imap")){
						iswgs = dest_iswgs;
						result.append(",\"desc\":\"校际巴士\"");
					}
					break;
				}
			}
			result.append("},");
		}
		result.deleteCharAt(result.lastIndexOf(","));
		result.append("],\"totalDistance\":"+total+"}");
		return result.toString();
	}
	
	
	public String caculate(Double x1,Double y1,Double x2,Double y2,String type) throws Exception{
		//如果少于100米，直接返回两点一线
				if(x1==null || y1 ==null ||x2==null||y2==null
						||x1==0 || y1 ==0 ||x2==0||y2==0) throw new Exception("{\"success\":false,\"message\":\"起点或终点坐标格式错误.\"}");
				Point start_point = (Point) reader.read(new StringBuffer("POINT(").append(x1).append(" ")
							.append(y1).append(")").toString());
				Point dest_point = (Point) reader.read(new StringBuffer("POINT(").append(x2).append(" ")
						.append(y2).append(")").toString());
				type = RouteType.BICYCLE.getCode().equals(type)
						||RouteType.AUTO.getCode().equals(type)?type:RouteType.FOOT.getCode();
				
				MapLayer startSchool = mapLayerService.getArea(start_point);
				MapLayer destSchool = mapLayerService.getArea(dest_point);
				boolean start_iswgs = false;
				if("EPSG:4490".equals(startSchool.getProjection())||"EPSG:4326".equals(startSchool.getProjection())){
					start_iswgs = true;
				}
				boolean dest_iswgs = false;
				if("EPSG:4490".equals(destSchool.getProjection())||"EPSG:4326".equals(destSchool.getProjection())){
					dest_iswgs = true;
				}
				List<MapPass> passes = null;
				boolean different = false;//不同校
				Point boarding = null;//上车点
				Point breakout = null;//下车点
				if(startSchool.getId().equals(destSchool.getId())){//同一校区
//					Double distance = dest_point.distance(start_point);
//					if(RouteType.FOOT.getCode().equals(type) && dest_point.distance(start_point)<50){
//						return "{\"routes\":[{\"route\":\"LINESTRING("+x1+" "+y1+","+x2+" "+y2+")\",\"length\":"+distance+"}],\"totalDistance\":"+distance+"}";
//					}
					passes = mapPassMapper.getAvailableByAreaId(type,startSchool.getId());
				}else{//跨校区
					different = true;
					//起点到乘车点
					passes = mapPassMapper.getAvailableByAreaId(type,startSchool.getId());
					
					//A校区到B校区
					Map<String,String> map = new HashMap<String,String>();
					map.put("fromId", startSchool.getId());
					map.put("toId", destSchool.getId());
					List<BusRoute> list = busRouteService.getBusRoutes(map);
					if(list == null || list.size()==0) throw new Exception("{\"success\":false,\"message\":\"跨校区，无校巴相连.\"}");
					if(list!=null && list.size()>0){
						BusRoute busRoute = list.get(0);
						String[] xs = busRoute.getXs().split(",");
						String[] ys = busRoute.getYs().split(",");
						StringBuffer path= new StringBuffer("LINESTRING(");
						for(int i=0;i<xs.length;i++){
							if (i >0) {
								path.append(",");
							}
							path.append(xs[i]+" "+ys[i]);
						}
						path.append(")");
						double[] bus_start = mapTransformService.wgs842utm(Double.valueOf(xs[0]), Double.valueOf(ys[0]));
						boarding = geometryFactory.createPoint(new Coordinate(bus_start[0],bus_start[1]));
						double[] bus_dest = mapTransformService.wgs842utm(Double.valueOf(xs[xs.length-1]), Double.valueOf(ys[ys.length-1]));
						breakout = geometryFactory.createPoint(new Coordinate(bus_dest[0],bus_dest[1]));
						MapPass e = new MapPass();
						e.setStart(bus_start[0]+","+bus_start[1]);
						e.setEnd(bus_dest[0]+","+bus_dest[1]);
						e.setPath(path.toString());
						e.setDriveWay("imap");
						Geometry busline = (Geometry)reader.read(path.toString());
						e.setLength(busline.getLength()*100000);//WGS84坐标
						passes.add(e);
					}
					//下车点到终点
					passes.addAll(mapPassMapper.getAvailableByAreaId(type,destSchool.getId()));
				}
				if(passes==null ||passes.size()==0)
					throw new Exception("{\"success\":false,\"message\":\"该校区尚未划定路网.\"}");
				//构建邻接矩阵,并分别找出里起点终点最近的通道
				Graphic g = new Graphic();
				Double away_start_shortest_distanc = Double.MAX_VALUE;
				Double away_dest_shortest_distanc = Double.MAX_VALUE;
				MapPass start_pass = null;
				MapPass dest_pass = null;
				
				Double away_boarding_shortest = Double.MAX_VALUE;
				Double away_breakout_shortest = Double.MAX_VALUE;
				MapPass boarding_pass = null;
				MapPass breakout_pass = null;
				for(int i=0;i<passes.size();i++){
					MapPass p = passes.get(i);
					LineString line = (LineString) reader.read(p.getPath());
					Double d1 = line.distance(start_point);
					Double d2 = line.distance(dest_point);	
					if(d1<away_start_shortest_distanc){
						away_start_shortest_distanc = d1;//离起点最近
						start_pass = p;
					}
					if(d2<away_dest_shortest_distanc){
						away_dest_shortest_distanc = d2;
						dest_pass = p;
					}
					if(different){
						Double d3 = line.distance(boarding);
						Double d4 = line.distance(breakout);
						if(d3<away_boarding_shortest){
							away_boarding_shortest = d3;//离上车点最近
							boarding_pass = p;
						}
						if(d4<away_breakout_shortest){
							away_breakout_shortest = d4;//离下车点最近
							breakout_pass = p;
						}
					}
					g.addVertex(p.getStart(),new Vertex(p.getEnd(), p.getLength()));
				}
				
				//最近出发点的两条道路（也可能是单向车道）
				List<MapPass> from_twoWays =mapPassMapper.get2Way(start_pass.getStart(),start_pass.getEnd());
				List<MapPass> to_twoWays =mapPassMapper.get2Way(dest_pass.getStart(),dest_pass.getEnd());
				for(MapPass pass:from_twoWays){
					//计算起点到最近道路的点(待完善，如果要考虑单向)
					List<MapPass> list = this.intersection(start_point, pass, false);//起点到附近最近的道路
					if(list!=null){
						passes.addAll(list);
						for(MapPass mapPass:list){
							g.addVertex(mapPass.getStart(),new Vertex(mapPass.getEnd(), mapPass.getLength()));
							for(MapPass pass2:to_twoWays){
								LineString line = (LineString) reader.read(pass.getPath());
								LineString line2 = (LineString) reader.read(pass2.getPath());
								if(pass2.getPath().equals(pass.getPath())){
									List<MapPass> list2 = intersection(dest_point,mapPass,true);
									passes.addAll(list2);
									for(MapPass mapPass2:list2){
										g.addVertex(mapPass2.getStart(),new Vertex(mapPass2.getEnd(), mapPass2.getLength()));
									}
								}else if(line.equalsExact(line2.reverse())){
									List<MapPass> list2 = intersection(dest_point,mapPass,true);
									passes.addAll(list2);
									for(MapPass mapPass2:list2){
										g.addVertex(mapPass2.getStart(),new Vertex(mapPass2.getEnd(), mapPass2.getLength()));
									}
								}else{
									passes.add(mapPass);
									g.addVertex(mapPass.getStart(),new Vertex(mapPass.getEnd(), mapPass.getLength()));
								}
							}
						}
					}
				}			
				for(MapPass pass:to_twoWays){
					//计算终点最近道路到终点(要考虑单向车道)
					List<MapPass> list = this.intersection(dest_point, pass, true);//附近最近的道路到终点
					if(list!=null){
						passes.addAll(list);
						for(MapPass mapPass:list){
							g.addVertex(mapPass.getStart(),new Vertex(mapPass.getEnd(), mapPass.getLength()));
						}
					}
				}
				Vertex dest = null;
				g.addVertex(x2+","+y2,dest);//确保终点也放到vertexs里
				
				if(boarding_pass != null &&	breakout_pass !=  null){
					List<MapPass> boarding_twoWays =mapPassMapper.get2Way(boarding_pass.getStart(),boarding_pass.getEnd());
					for(MapPass pass:boarding_twoWays){
						List<MapPass> list = this.intersection(boarding, pass, true);//附近最近的道路到上车点
						if(list!=null){
							passes.addAll(list);
							for(MapPass mapPass:list){
								g.addVertex(mapPass.getStart(),new Vertex(mapPass.getEnd(), mapPass.getLength()));
							}
						}
					}		
					
					List<MapPass> breakout_twoWays =mapPassMapper.get2Way(breakout_pass.getStart(),breakout_pass.getEnd());
					for(MapPass pass:breakout_twoWays){
						List<MapPass> list = this.intersection(breakout, pass, false);//下车点到附近最近的道路
						if(list!=null){
							passes.addAll(list);
							for(MapPass mapPass:list){
								g.addVertex(mapPass.getStart(),new Vertex(mapPass.getEnd(), mapPass.getLength()));
							}
						}
					}
					g.addVertex(breakout.getX()+","+breakout.getY(),dest);//确保下车点也放到vertexs里
				}
				
				//计算
				List<String> routes = g.getShortestPath(x1+","+y1, x2+","+y2);//["E","H","C","B"]
				StringBuffer result = new StringBuffer("{\"routes\":[");
				Double total = 0d;
				boolean iswgs = start_iswgs;
				//拼接为前台接收的格式
				for(int i =1; i<routes.size(); i++){
					String p = routes.get(i-1);
					String next = routes.get(i);			
					result.append("{");
					for(int j=0;j<passes.size();j++){
						MapPass pass = passes.get(j);
						if(p.equals(pass.getStart()) && next.equals(pass.getEnd())){
							result.append("\"route\":\"");
							//跨校区时，非WGS84坐标的路段全部转为WGS84格式的坐标
							result.append(different &&  !iswgs && !pass.getDriveWay().equals("imap")?
									this.transform(pass.getPath()).toString():pass.getPath());
							result.append("\",\"desc\":\"");
							result.append("\",\"startId\":\"");
							result.append(pass.getStart());
							result.append("\",\"endId\":\"");
							result.append(pass.getEnd());
							result.append("\",\"length\":");
							Double pLength=pass.getLength();
							//原来没有考虑WGS84环境中起点接入段和结尾接入段的情况
							if((i==1 || i==routes.size()-1) && pass.getStart()!=null){
							    if(pass.getStart().split(",")[0]!=null)
							        pLength=pass.getLength()*136075;//初略计算长度
                            }
							total +=pLength;
							result.append(pLength);
							if(different && pass.getDriveWay().equals("imap")){
								iswgs = dest_iswgs;
								result.append(",\"desc\":\"校际巴士\"");
							}
							break;
						}
					}
					result.append("},");
				}
				result.deleteCharAt(result.lastIndexOf(","));
				result.append("],\"totalDistance\":"+total+"}");
				return result.toString();
	}
	/**
	 * 将UTM坐标系下的路段转为WGS84坐标的路段（跨校区）
	 * @param path
	 * @return
	 * @throws ParseException
	 */
	private LineString transform(String path) throws ParseException {
		LineString line = (LineString)reader.read(path);
		CoordinateSequence seq = line.getCoordinateSequence();
		int len = line.getNumPoints();
		Coordinate[] points = new Coordinate[len];
		for(int i=0;i<len;i++){
			Coordinate c = seq.getCoordinate(i);
			double[] xy = mapTransformService.utm2wgs84(c.x, c.y);
			points[i]= new Coordinate(xy[0], xy[1]);
		}
		return geometryFactory.createLineString(points);
	}

	/**
	 * 将具体点（通过最近的道路）接通到邻接网络
	 * @param p
	 * @param mapPass
	 * @param passes
	 * @param g
	 * @throws ParseException
	 */
	public void connectGraph(Point p,MapPass mapPass,List<MapPass> passes,Graphic g) throws ParseException{
		List<MapPass> from_twoWays =mapPassMapper.get2Way(mapPass.getStart(),mapPass.getEnd());
		for(MapPass pass:from_twoWays){
			List<MapPass> list = this.intersection(p, pass, false);
			if(list!=null){
				passes.addAll(list);//回溯
				for(MapPass m:list){
					g.addVertex(m.getStart(),new Vertex(m.getEnd(), m.getLength()));
				}
			}
		}
	}

	/**
	 * 拼接每节通道结果json,返回每节通道的长度
	 * @param passes
	 * @param p
	 * @param next
	 * @return 
	 */
	public Double splice(List<MapPass> passes,String p,String next,StringBuffer result){
		result.append("{");
		Double length = 0d;
		for(int i=0;i<passes.size();i++){
			MapPass pass = passes.get(i);
			if(p.equals(pass.getStart()) && next.equals(pass.getEnd())){
				result.append("\"route\":\"");
				result.append(pass.getPath());
				result.append("\",\"startId\":\"");
				result.append(pass.getStart());
				result.append("\",\"endId\":\"");
				result.append(pass.getEnd());
				result.append("\",\"length\":");
				length = pass.getLength();
				result.append(length);
				break;
			}
		}
		result.append("}");
		return length;
	}
	
	/**
	 * mapPass被交点切割开的一部分==[交点,线段末点]
	 * @param point
	 * @param mapPass
	 * @param pointToLine true是点到线,false是线到点
	 * @return
	 * @throws ParseException
	 */
	public Map<String,Object> intersect(Point point,LineString l,boolean pointToLine) throws ParseException{
		//LineString l = (LineString) reader.read(mapPass.getPath());
		Point p = getNearestPoint(l, point);
		Coordinate[] array = new Coordinate[2];
		if(pointToLine){
			array[0] = new Coordinate(point.getX(), point.getY());
			array[1] = new Coordinate(p.getX(),p.getY());
		}else{
			array[0] = new Coordinate(p.getX(),p.getY());
			array[1] = new Coordinate(point.getX(), point.getY());
		}
		LineString l0 = geometryFactory.createLineString(array);
		MultiLineString lines = (MultiLineString)l.union(l0);
		int n =lines.getNumGeometries();
		Geometry segment = null;
		while(n>0){
			LineString temp = (LineString)lines.getGeometryN(n-1);
			//交点到线末
			if((pointToLine&& p.equalsExact(temp.getStartPoint())) || (!pointToLine&& p.equalsExact(temp.getEndPoint()))){
				segment = temp;
			}
			n--;
		}
		Map<String,Object> r = new HashMap<>();
		r.put("point", p);
		r.put("segment", segment);
		return r;
	}
	
	/**
	 * 求线段外的一点落在线段最近的点
	 * 垂点在线段上，那么垂点就是最近的点
	 * 垂点落在线段的延长线上，那么线段的起点或终点是最近的点
	 * @param line
	 * @return
	 */
	public Point getNearestPoint(LineString line,Point point){
		double x1 = point.getX(),y1 = point.getY();
		CoordinateSequence xys = line.getCoordinateSequence();
		double min = Double.MAX_VALUE;
		LineString l = null;
		for(int i=1, len=line.getNumPoints(); i<len; i++) {
		    LineString tempLine = geometryFactory.createLineString(new Coordinate[]{
		    		new Coordinate(xys.getCoordinate(i-1).x, xys.getCoordinate(i-1).y),new Coordinate(xys.getCoordinate(i).x,xys.getCoordinate(i).y)});
		    double distance = tempLine.distance(point);
		    if(distance < min) {
		    	min = distance;
				l = tempLine;
			}
		    if(distance == 0) break;
		}
		double[] symmetry = getSymmetryPoint(l.getStartPoint().getX(),l.getStartPoint().getY(),l.getEndPoint().getX(),l.getEndPoint().getY(),point);
		double x0 = symmetry[0];
		double y0 = symmetry[1];//对称点
		//连线（x1 y1）和（x y），与线段l进行交集计算
		LineString tempLine = geometryFactory.createLineString(new Coordinate[]{new Coordinate(x1, y1),new Coordinate(x0,y0)});
		
		boolean flag = tempLine.intersects(l);//计算与线段是否相交
		Point p = null;
		if(!flag){
			if(l.getStartPoint().distance(point)<l.getEndPoint().distance(point)){
				p = l.getStartPoint();
			}else
				p = l.getEndPoint();
		}else{
			p = (Point)tempLine.intersection(l);
		}
		return p;
	}
	
	/**
	 * 对称点
	 * @param x1 线段起点x
	 * @param y1 线段起点y
	 * @param x2 线段终点x
	 * @param y2 线段终点y
	 * @param point 线外一点
	 * @return
	 */
	public double[] getSymmetryPoint(double x1,double y1,double x2,double y2,Point point){
		double a = y2 - y1;//y2-y1;
		double b = x1 - x2;//x1-x2;
		double c = x2*y1 - x1*y2;//y1 * (x2 - x1) - x1 * (y2 - y1);
		//起点（x1,y1）到直线的距离绝对值
		double d = Math.abs((a * point.getX() + b * point.getY() + c)/Math.sqrt(Math.pow(a, 2)+Math.pow(b, 2)));//(x0,y0)到直线的距离
		//起点（x1,y1）关于直线的对称点(x0,y0)
		double y0 = point.getY() - (b/Math.sqrt(Math.pow(a, 2)+Math.pow(b, 2)))*2*d;
		double x0 = point.getX() - (a/Math.sqrt(Math.pow(a, 2)+Math.pow(b, 2)))*2*d;
		return new double[]{x0,y0};
	}
	/*//aX+bY+c=0
	double a = y2-y1;
	double b = x1-x2;
	double c = y1(x2-x1)-x1(y2-y1);
	//距离绝对值:
	double d = Math.abs((a * x0 + b * y0 + c)/Math.sqrt(Math.pow(a, 2)+Math.pow(b, 2)));//(x0,y0)到直线的距离
	//(x0,y0)关于直线【(y2-y1)x-(x2-x1)y-x1(y2-y1)+y1(x2-x1)=0】的对称点(x,y)
	double y = y0 - (b/Math.sqrt(Math.pow(a, 2)+Math.pow(b, 2)))*2d;
	double x = x0 - (a/Math.sqrt(Math.pow(a, 2)+Math.pow(b, 2)))*2d;*/
	public String caculateBAK20171114(Double x1,Double y1,Double x2,Double y2,String type) throws Exception{
		//如果少于100米，直接返回两点一线
		if(x1==null || y1 ==null ||x2==null||y2==null
				||x1==0 || y1 ==0 ||x2==0||y2==0) throw new Exception("{\"success\":false,\"message\":\"起点或终点坐标格式错误.\"}");
		Point start_point = (Point) reader.read(new StringBuffer("POINT(").append(x1).append(" ")
					.append(y1).append(")").toString());
		Point dest_point = (Point) reader.read(new StringBuffer("POINT(").append(x2).append(" ")
				.append(y2).append(")").toString());
		type = RouteType.BICYCLE.getCode().equals(type)
				||RouteType.AUTO.getCode().equals(type)?type:RouteType.FOOT.getCode();
		
		MapLayer startSchool = mapLayerService.getArea(start_point);
		MapLayer destSchool = mapLayerService.getArea(dest_point);
		boolean start_iswgs = false;
		if("EPSG:4490".equals(startSchool.getProjection())||"EPSG:4326".equals(startSchool.getProjection())){
			start_iswgs = true;
		}
		boolean dest_iswgs = false;
		if("EPSG:4490".equals(destSchool.getProjection())||"EPSG:4326".equals(destSchool.getProjection())){
			dest_iswgs = true;
		}
		List<MapPass> passes = null;
		boolean different = false;//不同校
		Point boarding = null;//上车点
		Point breakout = null;//下车点
		if(startSchool.getId().equals(destSchool.getId())){//同一校区
			passes = mapPassMapper.getAvailableByAreaId(type,startSchool.getId());
		}else{//跨校区
			different = true;
			//起点到乘车点
			passes = mapPassMapper.getAvailableByAreaId(type,startSchool.getId());
			
			//A校区到B校区
			Map<String,String> map = new HashMap<String,String>();
			map.put("fromId", startSchool.getId());
			map.put("toId", destSchool.getId());
			List<BusRoute> list = busRouteService.getBusRoutes(map);
			if(list == null || list.size()==0) throw new Exception("{\"success\":false,\"message\":\"跨校区，无校巴相连.\"}");
			if(list!=null && list.size()>0){
				BusRoute busRoute = list.get(0);
				String[] xs = busRoute.getXs().split(",");
				String[] ys = busRoute.getYs().split(",");
				StringBuffer path= new StringBuffer("LINESTRING(");
				for(int i=0;i<xs.length;i++){
					if (i >0) {
						path.append(",");
					}
					path.append(xs[i]+" "+ys[i]);
				}
				path.append(")");
				double[] bus_start = mapTransformService.wgs842utm(Double.valueOf(xs[0]), Double.valueOf(ys[0]));
				boarding = geometryFactory.createPoint(new Coordinate(bus_start[0],bus_start[1]));
				double[] bus_dest = mapTransformService.wgs842utm(Double.valueOf(xs[xs.length-1]), Double.valueOf(ys[ys.length-1]));
				breakout = geometryFactory.createPoint(new Coordinate(bus_dest[0],bus_dest[1]));
				MapPass e = new MapPass();
				e.setStart(bus_start[0]+","+bus_start[1]);
				e.setEnd(bus_dest[0]+","+bus_dest[1]);
				e.setPath(path.toString());
				e.setDriveWay("imap");
				Geometry busline = (Geometry)reader.read(path.toString());
				e.setLength(busline.getLength()*100000);//WGS84坐标
				passes.add(e);
			}
			//下车点到终点
			passes.addAll(mapPassMapper.getAvailableByAreaId(type,destSchool.getId()));
		}
		if(passes==null ||passes.size()==0)
			throw new Exception("{\"success\":false,\"message\":\"该校区尚未划定路网.\"}");
		//构建邻接矩阵,并分别找出里起点终点最近的通道
		Graphic g = new Graphic();
		Double away_start_shortest_distanc = Double.MAX_VALUE;
		Double away_dest_shortest_distanc = Double.MAX_VALUE;
		MapPass start_pass = null;
		MapPass dest_pass = null;
		
		Double away_boarding_shortest = Double.MAX_VALUE;
		Double away_breakout_shortest = Double.MAX_VALUE;
		MapPass boarding_pass = null;
		MapPass breakout_pass = null;
		for(int i=0;i<passes.size();i++){
			MapPass p = passes.get(i);
			LineString line = (LineString) reader.read(p.getPath());
			Double d1 = line.distance(start_point);
			Double d2 = line.distance(dest_point);	
			if(d1<away_start_shortest_distanc){
				away_start_shortest_distanc = d1;//离起点最近
				start_pass = p;
			}
			if(d2<away_dest_shortest_distanc){
				away_dest_shortest_distanc = d2;
				dest_pass = p;
			}
			if(different){//不同校区
				Double d3 = line.distance(boarding);
				Double d4 = line.distance(breakout);
				if(d3<away_boarding_shortest){
					away_boarding_shortest = d3;//离上车点最近
					boarding_pass = p;
				}
				if(d4<away_breakout_shortest){
					away_breakout_shortest = d4;//离下车点最近
					breakout_pass = p;
				}
			}
			g.addVertex(p.getStart(),new Vertex(p.getEnd(), p.getLength()));
		}
		
		List<MapPass> ways =mapPassMapper.get2Way(start_pass.getStart(),start_pass.getEnd());
		List<MapPass> ways2 =mapPassMapper.get2Way(dest_pass.getStart(),dest_pass.getEnd());
		Map<String,String> map =new HashMap<>();
		Set<String> set = new HashSet<>();
		for(MapPass p:ways){
			LineString l = (LineString) reader.read(p.getPath());
			Map<String,Object> union = intersect(start_point,l,true);
			Point n1 = (Point)union.get("point");//n1为线段l距离start_point最近的点
			LineString start_line = geometryFactory.createLineString(new Coordinate[]{new Coordinate(x1, y1),n1.getCoordinate()});
			if(!(map.containsKey(x1+","+y1) && map.get(x1+","+y1).equals(n1.getX()+","+n1.getY()))){
				map.put(x1+","+y1, n1.getX()+","+n1.getY());
				//segs.add(start_line);
				MapPass pass = new MapPass();
				pass.setFootWay("1");
				pass.setDriveWay("0");
				pass.setCycleWay("0");
				pass.setStart(start_line.getStartPoint().getX()==l.getStartPoint().getX()&&start_line.getStartPoint().getY()==l.getStartPoint().getY()?
						p.getStart():(start_line.getStartPoint().getX()==l.getEndPoint().getX()&&start_line.getStartPoint().getY()==l.getEndPoint().getY()?
								p.getEnd():x1+","+y1));
				pass.setEnd(start_line.getEndPoint().getX()==l.getStartPoint().getX()&&start_line.getEndPoint().getY()==l.getStartPoint().getY()?
						p.getStart():(start_line.getEndPoint().getX()==l.getEndPoint().getX()&&start_line.getEndPoint().getY()==l.getEndPoint().getY()?
								p.getEnd():n1.getX()+","+n1.getY()));
				pass.setLength(start_line.getLength());
				pass.setPath(start_line.toString());
				passes.add(pass);
				g.addVertex(pass.getStart(),new Vertex(pass.getEnd(), pass.getLength()));
			}
			LineString segment = (LineString)union.get("segment");
			if(segment == null) continue;
			for(MapPass pp:ways2){
				if(p.getPath().equals(pp.getPath())){//保证方向一致
					set.add(pp.getId());
					//LineString ll = (LineString) reader.read(pp.getPath());
					Map<String,Object> union2 = intersect(dest_point,l,false);//
					Point n2 = (Point)union2.get("point");//n1为线段l距离start_point最近的点
					LineString dest_line = geometryFactory.createLineString(new Coordinate[]{n2.getCoordinate(),new Coordinate(x2, y2),});
					if(!(map.containsKey(n2.getX()+","+n2.getY()) && map.get(n2.getX()+","+n2.getY()).equals(x2+","+y2))){
						map.put(n2.getX()+","+n2.getY(), x2+","+y2);
						//segs.add(dest_line);
						MapPass pass = new MapPass();
						pass.setFootWay("1");
						pass.setDriveWay("0");
						pass.setCycleWay("0");
						pass.setStart(dest_line.getStartPoint().getX()==l.getStartPoint().getX()&&dest_line.getStartPoint().getY()==l.getStartPoint().getY()?
								p.getStart():(dest_line.getStartPoint().getX()==l.getEndPoint().getX()&&dest_line.getStartPoint().getY()==l.getEndPoint().getY()?
										p.getEnd():n2.getX()+","+n2.getY()));
						pass.setEnd(dest_line.getEndPoint().getX()==l.getStartPoint().getX()&&dest_line.getEndPoint().getY()==l.getStartPoint().getY()?
								p.getStart():(dest_line.getEndPoint().getX()==l.getEndPoint().getX()&&dest_line.getEndPoint().getY()==l.getEndPoint().getY()?
										p.getEnd():x2+","+y2));
						pass.setLength(dest_line.getLength());
						pass.setPath(dest_line.toString());
						passes.add(pass);
						g.addVertex(pass.getStart(),new Vertex(pass.getEnd(), pass.getLength()));
					}
					LineString segment2 = (LineString)union2.get("segment");
					if(segment2 ==null) continue;
					LineString segment_line = null;
					if(segment.equalsExact(segment2)) segment_line =segment; //segs.add(segment);
					else{
						if(segment.contains(segment2)){
							//segs.add(segment.difference(segment2));
							segment_line = (LineString)segment.difference(segment2);
						}else{
							//segs.add(segment2.difference(segment));
							segment_line = (LineString)segment2.difference(segment);
						}
					}
						
					MapPass pass = new MapPass();
					pass.setFootWay("1");
					pass.setDriveWay("0");
					pass.setCycleWay("0");
					pass.setStart(segment_line.getStartPoint().getX()==l.getStartPoint().getX()&&segment_line.getStartPoint().getY()==l.getStartPoint().getY()?
							p.getStart():(segment_line.getStartPoint().getX()==l.getEndPoint().getX()&&segment_line.getStartPoint().getY()==l.getEndPoint().getY()?
									p.getEnd():segment_line.getStartPoint().getX()+","+segment_line.getStartPoint().getY()));
					pass.setEnd(segment_line.getEndPoint().getX()==l.getStartPoint().getX()&&segment_line.getEndPoint().getY()==l.getStartPoint().getY()?
							p.getStart():(segment_line.getEndPoint().getX()==l.getEndPoint().getX()&&segment_line.getEndPoint().getY()==l.getEndPoint().getY()?
									p.getEnd():segment_line.getEndPoint().getX()+","+segment_line.getEndPoint().getY()));
					pass.setLength(segment_line.getLength());
					pass.setPath(segment_line.toString());
					passes.add(pass);
					g.addVertex(pass.getStart(),new Vertex(pass.getEnd(), pass.getLength()));
				}
			}
		}
		for(MapPass p:ways2){
			if(set.contains(p.getId())) continue;
			LineString l = (LineString) reader.read(p.getPath());
			Map<String,Object> union2 = intersect(dest_point,l,false);//
			Point n2 = (Point)union2.get("point");//n2为线段l距离dest_point最近的点
			LineString dest_line = geometryFactory.createLineString(new Coordinate[]{n2.getCoordinate(),new Coordinate(x2, y2),});
			if(!(map.containsKey(n2.getX()+","+n2.getY()) && map.get(n2.getX()+","+n2.getY()).equals(x2+","+y2))){
				map.put(n2.getX()+","+n2.getY(), x2+","+y2);
				//segs.add(dest_line);
				MapPass pass = new MapPass();
				pass.setFootWay("1");
				pass.setDriveWay("0");
				pass.setCycleWay("0");
				pass.setStart(dest_line.getStartPoint().getX()==l.getStartPoint().getX()&&dest_line.getStartPoint().getY()==l.getStartPoint().getY()?
						p.getStart():(dest_line.getStartPoint().getX()==l.getEndPoint().getX()&&dest_line.getStartPoint().getY()==l.getEndPoint().getY()?
								p.getEnd():n2.getX()+","+n2.getY()));
				pass.setEnd(dest_line.getEndPoint().getX()==l.getStartPoint().getX()&&dest_line.getEndPoint().getY()==l.getStartPoint().getY()?
						p.getStart():(dest_line.getEndPoint().getX()==l.getEndPoint().getX()&&dest_line.getEndPoint().getY()==l.getEndPoint().getY()?
								p.getEnd():x2+","+y2));
				pass.setLength(dest_line.getLength());
				pass.setPath(dest_line.toString());
				passes.add(pass);
				g.addVertex(pass.getStart(),new Vertex(pass.getEnd(), pass.getLength()));
			}
			//segs.add((LineString)union2.get("segment"));
			LineString segment_line = (LineString)union2.get("segment");
			if(segment_line == null) continue;
			MapPass pass = new MapPass();
			pass.setFootWay("1");
			pass.setDriveWay("0");
			pass.setCycleWay("0");
			pass.setStart(segment_line.getStartPoint().getX()==l.getStartPoint().getX()&&segment_line.getStartPoint().getY()==l.getStartPoint().getY()?
					p.getStart():(segment_line.getStartPoint().getX()==l.getEndPoint().getX()&&segment_line.getStartPoint().getY()==l.getEndPoint().getY()?
							p.getEnd():segment_line.getStartPoint().getX()+","+segment_line.getStartPoint().getY()));
			pass.setEnd(segment_line.getEndPoint().getX()==l.getStartPoint().getX()&&segment_line.getEndPoint().getY()==l.getStartPoint().getY()?
					p.getStart():(segment_line.getEndPoint().getX()==l.getEndPoint().getX()&&segment_line.getEndPoint().getY()==l.getEndPoint().getY()?
							p.getEnd():segment_line.getEndPoint().getX()+","+segment_line.getEndPoint().getY()));
			pass.setLength(segment_line.getLength());
			pass.setPath(segment_line.toString());
			passes.add(pass);
			g.addVertex(pass.getStart(),new Vertex(pass.getEnd(), pass.getLength()));
		}
		Vertex dest = null;
		g.addVertex(x2+","+y2,dest);//确保终点也放到vertexs里
		
		if(boarding_pass != null &&	breakout_pass !=  null){
			List<MapPass> boarding_twoWays =mapPassMapper.get2Way(boarding_pass.getStart(),boarding_pass.getEnd());
			for(MapPass pass:boarding_twoWays){
				List<MapPass> list = this.intersection(boarding, pass, true);//附近最近的道路到上车点
				if(list!=null){
					passes.addAll(list);
					for(MapPass mapPass:list){
						g.addVertex(mapPass.getStart(),new Vertex(mapPass.getEnd(), mapPass.getLength()));
					}
				}
			}		
			
			List<MapPass> breakout_twoWays =mapPassMapper.get2Way(breakout_pass.getStart(),breakout_pass.getEnd());
			for(MapPass pass:breakout_twoWays){
				List<MapPass> list = this.intersection(breakout, pass, false);//下车点到附近最近的道路
				if(list!=null){
					passes.addAll(list);
					for(MapPass mapPass:list){
						g.addVertex(mapPass.getStart(),new Vertex(mapPass.getEnd(), mapPass.getLength()));
					}
				}
			}
			g.addVertex(breakout.getX()+","+breakout.getY(),dest);//确保下车点也放到vertexs里
		}
		//计算
		List<String> routes = g.getShortestPath(x1+","+y1, x2+","+y2);//["E","H","C","B"]
		StringBuffer result = new StringBuffer("{\"routes\":[");
		Double total = 0d;
		boolean iswgs = start_iswgs;
		//拼接为前台接收的格式
		for(int i =1; i<routes.size(); i++){
			String p = routes.get(i-1);
			String next = routes.get(i);			
			result.append("{");
			for(int j=0;j<passes.size();j++){
				MapPass pass = passes.get(j);
				if(p.equals(pass.getStart()) && next.equals(pass.getEnd())){
					result.append("\"route\":\"");
					//跨校区时，非WGS84坐标的路段全部转为WGS84格式的坐标
					result.append(different &&  !iswgs && !pass.getDriveWay().equals("imap")?
							this.transform(pass.getPath()).toString():pass.getPath());
					result.append("\",\"desc\":\"");
					result.append("\",\"startId\":\"");
					result.append(pass.getStart());
					result.append("\",\"endId\":\"");
					result.append(pass.getEnd());
					result.append("\",\"length\":");
					total += pass.getLength();
					result.append(pass.getLength());
					if(different && pass.getDriveWay().equals("imap")){
						iswgs = dest_iswgs;
						result.append(",\"desc\":\"校际巴士\"");
					}
					break;
				}
			}
			result.append("},");
		}
		result.deleteCharAt(result.lastIndexOf(","));
		result.append("],\"totalDistance\":"+total+"}");
		return result.toString();
	}
	
	/**
	 * 构建通道（起点到最近起点的通道/最近终点的通道到终点）
	 * @param point 点与线的交点
	 * @param mapPass
	 * @param clockwise true为线到点，false为点到线
	 * @return
	 * @throws ParseException
	 */
	public List<MapPass> intersection(Point point,MapPass mapPass,boolean clockwise) throws ParseException{
		LineString line = (LineString) reader.read(mapPass.getPath());
		List<MapPass> list = new ArrayList<>();
		Map<String,Object> result = distanceTo(point, line, true);
		//1、line开端==交点
		if((int)result.get("index") ==0 && (double)result.get("along")<=0){
			Coordinate startPoint = line.getStartPoint().getCoordinate();
			LineString start_line = geometryFactory.createLineString(new Coordinate[]{point.getCoordinate(),startPoint});
			MapPass start_pass = new MapPass();
			start_pass.setFootWay("1");
			start_pass.setDriveWay("0");
			start_pass.setCycleWay("0");
			if(!clockwise){//计算[线到点]》》起点相同，线的开端到终点
				start_line.reverse();
				start_pass.setStart(mapPass.getStart());
				start_pass.setEnd(point.getX()+","+point.getY());//终点（目的地）
			}else{//计算[点到线]》》起点到线的开端
				start_pass.setStart(point.getX()+","+point.getY());//起点（出发地）
				start_pass.setEnd(mapPass.getStart());
			}
			start_pass.setLength(start_line.getLength());
			start_pass.setPath(start_line.toString());
			list.add(start_pass);
			return list;
		}
		//2、line末端==交点
		if((int)result.get("index") ==line.getNumPoints()-1 && (double)result.get("along")>=1){
			Coordinate endPoint = line.getEndPoint().getCoordinate();
			LineString start_line = geometryFactory.createLineString(new Coordinate[]{point.getCoordinate(),endPoint});
			MapPass start_pass = new MapPass();
			start_pass.setFootWay("1");
			start_pass.setDriveWay("0");
			start_pass.setCycleWay("0");
			if(clockwise){//计算[线到点]》》线的末端到终点
				start_line.reverse();
				start_pass.setStart(mapPass.getEnd());
				start_pass.setEnd(point.getX()+","+point.getY());//终点
			}else{//计算[点到线]》》起点到线的末端
				start_pass.setStart(point.getX()+","+point.getY());//起点
				start_pass.setEnd(mapPass.getEnd());
			}
			start_pass.setLength(start_line.getLength());
			start_pass.setPath(start_line.toString());
			list.add(start_pass);
			return list;
		}		
		//3、line中间某点==交点
		Coordinate[] xys = line.getCoordinates();
		Coordinate[] left = new Coordinate[(int)result.get("index")+2];//序号+1=原长度，原长度+1（交点）=新长度
		System.arraycopy(xys, 0, left, 0, (int)result.get("index")+1);
		Coordinate crossCoordinate = new Coordinate((double)result.get("x"),(double)result.get("y"));//交点
		left[left.length-1] = crossCoordinate;
		LineString left_line = geometryFactory.createLineString(left);
		Coordinate[] right = new Coordinate[xys.length-left.length+2];
		right[0] = crossCoordinate;
		System.arraycopy(xys, (int)result.get("index")+1, right, 1,(xys.length-left.length+1));
		LineString right_line = geometryFactory.createLineString(right);
		//LineString right_line = (LineString) line.difference(left_line);
		MapPass start_pass = new MapPass();
		LineString start_line = geometryFactory.createLineString(new Coordinate[]{
				point.getCoordinate(),crossCoordinate});
		start_pass.setFootWay("1");
		start_pass.setDriveWay("0");
		start_pass.setCycleWay("0");
		if(clockwise){//计算[线到点]》》线的开端到交叉点，交叉点到终点
			start_line.reverse();
			start_pass.setStart(crossCoordinate.x+","+crossCoordinate.y);//交叉点
			start_pass.setEnd(point.getX()+","+point.getY());//终点
			MapPass start_cross_pass = new MapPass();//线的开端到交叉点，交叉点到终点
			start_cross_pass.setFootWay("1");
			start_cross_pass.setDriveWay("0");
			start_cross_pass.setCycleWay("0");
			start_cross_pass.setStart(mapPass.getStart());
			start_cross_pass.setEnd(crossCoordinate.x+","+crossCoordinate.y);
			start_cross_pass.setLength(left_line.getLength());
			start_cross_pass.setPath(left_line.toString());
			list.add(start_cross_pass);
		}else{//计算[点到线]》》起点到线的交叉点，交叉点到线的末端
			start_pass.setStart(point.getX()+","+point.getY());//起点
			start_pass.setEnd(crossCoordinate.x+","+crossCoordinate.y);//交叉点
			MapPass cross_end_pass = new MapPass();//交叉点到线的末端
			cross_end_pass.setFootWay("1");
			cross_end_pass.setDriveWay("0");
			cross_end_pass.setCycleWay("0");
			cross_end_pass.setStart(crossCoordinate.x+","+crossCoordinate.y);
			cross_end_pass.setEnd(mapPass.getEnd());
			cross_end_pass.setLength(right_line.getLength());
			cross_end_pass.setPath(right_line.toString());
			list.add(cross_end_pass);
		}
		start_pass.setLength(start_line.getLength());
		start_pass.setPath(start_line.toString());
		list.add(start_pass);
		return list;
	}
	
	/**
	 * 计算点和线的距离，返回点与线距离最近的点的x，y；
	 * 如果detail为true，还附带返回点与线距离最近的点的x，y
	 * @param p
	 * @param line
	 * @param detail
	 * @return
	 */
	public Map<String,Object> distanceTo(Point p,LineString line,Boolean detail){
		if(p==null || line == null || p.isEmpty() || line.isEmpty()) return null;
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("distance", p.distance(line));
		if(detail){
			CoordinateSequence xys = line.getCoordinateSequence();
			double min = Double.MAX_VALUE;
			int index = 0;
			for(int i=1, len=line.getNumPoints(); i<len; i++) {
				double x0 = p.getX();
			    double y0 = p.getY();
			    double x1 = xys.getCoordinate(i-1).x;
			    double y1 = xys.getCoordinate(i-1).y;
			    double x2 = xys.getCoordinate(i).x;
			    double y2 = xys.getCoordinate(i).y;
			    double dx = x2 - x1;
			    double dy = y2 - y1;
			    double along = (dx == 0 && dy == 0) ? 0 : ((dx * (x0 - x1)) + (dy * (y0 - y1))) /
		                (Math.pow(dx, 2) + Math.pow(dy, 2));
			    double x, y;
			    index++;
			    if(along <= 0) {//i-1 到 i 这段的左边
			        x = x1;
			        y = y1;
			        index--;
			    } else if(along >= 1) {//i-1 到 i 这段的右边
			        x = x2;
			        y = y2;
			    } else {//落在i-1 到 i 这段的中间
			        x = x1 + along * dx;
			        y = y1 + along * dy;
			        index--;
			    }
			    
			    double distance = Math.pow(x - x0, 2) + Math.pow(y - y0, 2);
			    if(distance < min) {
			    	min = distance;
			    	result.put("along", along);
			    	result.put("index", index);
			    	result.put("x", x);
				    result.put("y", y);
			    }
			    if(min == 0) {
			    	break;
			    }
			}
			result.put("distance", min);
		}
		return result;
	}
	
	public Coordinate distMath(double x,double y,double x1,double y1,double x2,double y2){
		double se = (x1-x2)*(x1-x2)+(y1-y2)*(y1-y2);//线段两点距离平方
		double p = ((x-x1)*(x2-x1)+(y-y1)*(y2-y1));//向量点乘=|a|*|b|*cosA
		double r = p/se;//r即点到线段的投影长度与线段长度比
		double outx=x1+r*(x2-x1);//垂足x
		double outy=y1+r*(y2-y1);//垂足y
		//double des =Math.sqrt((x-outx)*(x-outx)+(y-outy)*(y-outy));//与垂足距离
		return new Coordinate(outx, outy);
	}
	
	
	public static void main(String[] args) throws ParseException {
		RoadPlanningServiceImpl s = new RoadPlanningServiceImpl();
		LineString line = s.geometryFactory.createLineString(new Coordinate[]{
				new Coordinate(2,1),new Coordinate(3,1),new Coordinate(5,1),new Coordinate(8,1),new Coordinate(10,1),new Coordinate(12,1)
		});
		LineString line2 = s.geometryFactory.createLineString(new Coordinate[]{
				new Coordinate(2,5),new Coordinate(2,1)
		});
		LineString line3 = s.geometryFactory.createLineString(new Coordinate[]{
				new Coordinate(8,1),new Coordinate(8,8)
		});
		LineString line4 = s.geometryFactory.createLineString(new Coordinate[]{
				new Coordinate(8,1),new Coordinate(5,1),new Coordinate(3,1),new Coordinate(2,1),new Coordinate(0,1)
		});
		System.out.println(line.union(line2));
		System.out.println(line3.union(line4));
		Geometry u = line.union(line2).union(line3).union(line4);
		System.out.println(line.union(line2).union(line3).union(line4));
		Geometry d1 = line.difference(line.difference(line3.union(line4)));
		System.out.println(line2.difference(s.geometryFactory.createPoint(new Coordinate(2,3))));
		//两点式转一般式
//				(y-y1)/(y2-y1)=(x-x1)/(x2-x1)
//				(y-y1)(x2-x1)=(x-x1)(y2-y1)
//				(y2-y1)x-(x2-x1)y-x1(y2-y1)+y1(x2-x1)=0
		/*//aX+bY+c=0
		double a = y2-y1;
		double b = x1-x2;
		double c = y1(x2-x1)-x1(y2-y1);
		//距离绝对值:
		double d = Math.abs((a * x0 + b * y0 + c)/Math.sqrt(Math.pow(a, 2)+Math.pow(b, 2)));//(x0,y0)到直线的距离
		//(x0,y0)关于直线【(y2-y1)x-(x2-x1)y-x1(y2-y1)+y1(x2-x1)=0】的对称点(x,y)
		double y = y0 - (b/Math.sqrt(Math.pow(a, 2)+Math.pow(b, 2)))*2d;
		double x = x0 - (a/Math.sqrt(Math.pow(a, 2)+Math.pow(b, 2)))*2d;*/
	}
	
}
