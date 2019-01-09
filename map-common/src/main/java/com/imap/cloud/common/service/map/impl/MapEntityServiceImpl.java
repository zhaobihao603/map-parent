package com.imap.cloud.common.service.map.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.imap.cloud.common.cache.RedisCache;
import com.imap.cloud.common.entity.map.MapLayer;
import com.imap.cloud.common.service.map.MapEntityService;
import com.imap.cloud.common.service.map.MapLayerService;
import com.imap.cloud.common.service.system.DictionaryService;
import com.imap.cloud.common.util.HttpClientUtil;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

@Service
public class MapEntityServiceImpl implements MapEntityService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
	private WKTReader reader = new WKTReader( geometryFactory );
	@Autowired
	private RedisCache cache;
	private @Autowired DictionaryService dictionaryService;
	private @Autowired MapLayerService mapLayerService;
	
	public String query(String name,String area,Integer max){
		List<MapLayer> layers = mapLayerService.getEntityLayer();
		if(layers == null || layers.size()==0) return null;
		MapLayer entityLayer = layers.get(0);
		StringBuffer url = new StringBuffer(entityLayer.getUrl());//"http://192.168.1.112:8081/geoserver
		String areaName = null;
		try {
			areaName = StringUtils.isEmpty(area)?null:mapLayerService.selectByPk(area).getName();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String featureType = entityLayer.getFeatureType();
		url.append("?service=WFS&version=1.0.0&request=GetFeature&outputformat=json&typeName=").append(featureType);
		url.append("&maxFeatures=").append(max==null||max<1?Integer.MAX_VALUE:max).append("&Filter=");
		if(StringUtils.isNotBlank(name) &&StringUtils.isBlank(areaName))url.append(this.setFilter("name",name));
		if(StringUtils.isBlank(name) &&StringUtils.isNotBlank(areaName))url.append(this.setFilter("sname",areaName));
		if(StringUtils.isNotBlank(name) &&StringUtils.isNotBlank(areaName)){
			String filter = "<wfs:GetFeature xmlns:wfs='http://www.opengis.net/wfs' service='WFS' version='1.0.0' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xsi:schemaLocation='http://www.opengis.net/wfs http://schemas.opengis.net/wfs/1.0.0/WFS-transaction.xsd'>" +
				"<wfs:Query>" +
				"<ogc:Filter xmlns:ogc='http://www.opengis.net/ogc'><ogc:And>" +
				"<ogc:PropertyIsLike wildCard='*' singleChar='.' escape='!'><ogc:PropertyName>sname</ogc:PropertyName>" +
				"<ogc:Literal>*"+areaName+"*</ogc:Literal></ogc:PropertyIsLike>" +
				"<ogc:PropertyIsLike wildCard='*' singleChar='.' escape='!'><ogc:PropertyName>name</ogc:PropertyName>" +
				"<ogc:Literal>*"+name+"*</ogc:Literal></ogc:PropertyIsLike></ogc:And></ogc:Filter></wfs:Query></wfs:GetFeature>";
			try {
				filter = URLEncoder.encode(filter, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			url.append(filter);
		}
		return HttpClientUtil.sendHttpGet(url.toString());
	}
	
	public String query(String name, String type, String area, Integer max){
		List<MapLayer> layers = mapLayerService.getEntityLayer();
		if(layers == null || layers.size()==0) return null;
		MapLayer entityLayer = layers.get(0);
		StringBuffer url = new StringBuffer(entityLayer.getUrl());//"http://192.168.1.112:8081/geoserver
		String areaName = null;
		try {
			areaName = StringUtils.isEmpty(area)?null:mapLayerService.selectByPk(area).getName();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String featureType = entityLayer.getFeatureType();
		url.append("?service=WFS&version=1.0.0&request=GetFeature&outputformat=json&typeName=").append(featureType);
		url.append("&maxFeatures=").append(max==null||max<1?Integer.MAX_VALUE:max);		
		String nameFilter = getFilter("name",name);
		String typeFilter = getFilter("entityType",type);
		String areaNameFilter = getFilter("sname",areaName);
		if(nameFilter!=null || typeFilter!=null || areaNameFilter!=null){
			StringBuffer filter = new StringBuffer("<wfs:GetFeature xmlns:wfs='http://www.opengis.net/wfs' service='WFS' version='1.0.0' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xsi:schemaLocation='http://www.opengis.net/wfs http://schemas.opengis.net/wfs/1.0.0/WFS-transaction.xsd'>");
			filter.append("<wfs:Query><ogc:Filter xmlns:ogc='http://www.opengis.net/ogc'><ogc:And>");
			filter.append(nameFilter == null?"":nameFilter);
			filter.append(typeFilter == null?"":typeFilter);
			filter.append(areaNameFilter == null?"":areaNameFilter);
			filter.append("</ogc:And></ogc:Filter></wfs:Query></wfs:GetFeature>");
			try {
				url.append("&Filter="+URLEncoder.encode(filter.toString(), "utf-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}		
		return HttpClientUtil.sendHttpGet(url.toString());
	}

	private static String getFilter(String propertyname,String literal){
		if(StringUtils.isBlank(literal)) return null;
		String filter = "<ogc:PropertyIsLike wildCard='*' singleChar='.' escape='!'><ogc:PropertyName>"+propertyname
					+"</ogc:PropertyName><ogc:Literal>*"+literal
					+"*</ogc:Literal></ogc:PropertyIsLike>";
		return filter;
	}
	
	private String setFilter(String propertyname,String literal){
		try{
			String filter ="<wfs:GetFeature xmlns:wfs='http://www.opengis.net/wfs' service='WFS' version='1.0.0' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xsi:schemaLocation='http://www.opengis.net/wfs http://schemas.opengis.net/wfs/1.0.0/WFS-transaction.xsd'>" +
				"<wfs:Query>" +
				"<ogc:Filter xmlns:ogc='http://www.opengis.net/ogc'>" +
				"<ogc:PropertyIsLike wildCard='*' singleChar='.' escape='!'><ogc:PropertyName>"+propertyname+"</ogc:PropertyName>" +
				"<ogc:Literal>*"+literal+"*</ogc:Literal></ogc:PropertyIsLike></ogc:Filter></wfs:Query></wfs:GetFeature>";
			return URLEncoder.encode(filter, "utf-8");
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	
	public String selectAllFromWFS(){
		List<MapLayer> layers = mapLayerService.getEntityLayer();
		if(layers == null || layers.size()==0) return null;
		MapLayer entityLayer = layers.get(0);
		StringBuffer url = new StringBuffer(entityLayer.getUrl());
		url.append("?service=WFS&version=1.0.0&request=GetFeature&outputformat=json&typeName=").append(entityLayer.getFeatureType());
		url.append("&maxFeatures=").append(Integer.MAX_VALUE);
		String str = HttpClientUtil.sendHttpGet(url.toString());
		return str;
	}
	
	public String queryNearBy(Double x,Double y,Double distance){
		List<MapLayer> layers = mapLayerService.getEntityLayer();
		if(layers == null || layers.size()==0) return null;
		MapLayer entityLayer = layers.get(0);
		StringBuffer url = new StringBuffer(entityLayer.getUrl());
		url.append("?service=WFS&version=1.0.0&request=GetFeature&outputformat=json&maxFeatures=50&typeName=").append(entityLayer.getFeatureType());//IMAP:zheda_hotmap
		url.append("&BBOX=");
		if(distance==null || distance<0) {
		}
		else {
			if(x>180 && y>90)
				distance = 50d;
			else
				distance=0.0002;
		}
		url.append(x-distance);
		url.append("%2C");
		url.append(y-distance);
		url.append("%2C");
		url.append(x+distance);
		url.append("%2C");		
		url.append(y+distance);
		String json = HttpClientUtil.sendHttpGet(url.toString());
		if(json!=null){
			JSONObject obj = JSON.parseObject(json);
			JSONArray array = obj.getJSONArray("features");
			double m = Double.MAX_VALUE;
			JSONObject f =null; 
			try {
				Point point = (Point) reader.read("POINT ("+x+" "+y+")");
				for(int i=0;array!=null && i<array.size();i++){
					JSONObject feature = array.getJSONObject(i);
					JSONObject o = feature.getJSONObject("geometry");
					String type = o.getString("type");
					String a = o.getString("coordinates");
					a = a.substring(1, a.length()-1);
					String wkt = type+ a.replace("],[", "*").replace("[", "(").replace("]", ")").replace(",", " ").replace("*", ",");
					Geometry polygon = (Geometry)reader.read(wkt);
					double d = polygon.distance(point);
					if(m>d){ f = feature;m =d;}
				}
				if(f!=null){
					obj.remove("features");
					JSONArray list = new JSONArray();
					list.add(f);				
					obj.put("features", list);
					obj.put("totalFeatures", 1);
				}				
				return obj.toJSONString();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return json;
	}
}
