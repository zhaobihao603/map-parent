package com.imap.cloud.common.util.xsdreader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


/**   
 * @Title: TestDom4j.java
 * @Package 
 * @Description: 解析xml字符串
 * @author 无处不在
 * @date 2012-11-20 下午05:14:05
 * @version V1.0   
 */
public class WFSReader {
	
	public static List<Map<String,String>> gradeWfsFeatureValue(String xml,Integer classes,String... filters){
		List<Map<String,String>> grade = new ArrayList<Map<String,String>>();
		List<Double> gradeVals = new ArrayList<Double>();
		List<Map<String,String>>vals = readWfsFeatureValue(xml);
		for (Map<String, String> map : vals) {
			gradeVals.add(Double.parseDouble(map.get(filters[0])));
		}
		Object[] obj = gradeVals.toArray();
		Arrays.sort(obj);
		Double interval = ((Double) obj[obj.length-1])/classes;
		Double temp = 0d;
		for (int i = 0; i < classes; i++) {
			Map<String,String> map = new HashMap<String,String>();
			BigDecimal b = new BigDecimal(temp+interval);
			Double next = b.setScale(3, BigDecimal.ROUND_UP).doubleValue();
			map.put(temp+"-"+next,temp+"-"+next);
			grade.add(map);
			temp = next;
		}
		return grade;
	}
	
	/**
	 * 分组获取WFS服务的属性值
	 * @param xml
	 * @return
	 */
	public static List<Map<String,String>> groupWfsFeatureValue(String xml,String... filters){
		List<Map<String,String>> group = new ArrayList<Map<String,String>>();
		Map<String,Map<String,String>>groupVals = new HashMap<String,Map<String,String>>();
		List<Map<String,String>>vals = readWfsFeatureValue(xml);
		String filterKey = "";
		for (int i = 0; i < filters.length; i++) {
			if(i==0){
				filterKey+=filters[i];
			}else{
				filterKey+=","+filters[i];
			}
		}
		for (Map<String, String> map : vals) {
			String groupValKey = "";
			for (int i = 0; i < filters.length; i++) {
				if(i==0){
					groupValKey+=map.get(filters[i]);
				}else{
					groupValKey+="_"+map.get(filters[i]);
				}
			}
			if(groupVals.containsKey(groupValKey)){
				Map<String,String> groupVal = groupVals.get(groupValKey);
				group.remove(groupVal);
				groupVal.put("count",String.valueOf( Integer.parseInt(groupVal.get("count"))+1));
				groupVals.put(groupValKey, groupVal);
				group.add(groupVal);
			}else{
				Map<String,String> groupVal = new HashMap<String,String>();
				String filterValue = "";
				for (int i = 0; i < filters.length; i++) {
					if(i==0){
						filterValue+= map.get(filters[i]);
					}else{
						filterValue+=","+map.get(filters[i]);
					}
				}
				groupVal.put(filterKey, filterValue);
				groupVal.put("count", "1");
				groupVals.put(groupValKey, groupVal);
				group.add(groupVal);
			}
		}
		return group;
	}
	
	/**
	 * 根据WFS服务的XML字符串获取WFS的属性值(xml为json字符串)
	 * @param xml
	 * @return
	 */
	public static List<Map<String,String>> readWfsFeatureValue(String xml){
		List<Map<String,String>> vals = new ArrayList<Map<String,String>>();
		JSONObject json = JSONObject.parseObject(xml);
		JSONArray properties = json.getJSONArray("features");
		try{
			for (Object node : properties) {
				JSONObject joAttr = JSONObject.parseObject(node.toString());
				JSONObject jo = joAttr.getJSONObject("properties");
				Map<String,String> fieldMap = new HashMap<String,String>();
				for (Map.Entry<String, Object> entry : jo.entrySet()) {
					fieldMap.put(entry.getKey(), entry.getValue().toString());
				}
				vals.add(fieldMap);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return vals;
	}
	
	/**
	 * 根据WFS服务的XML字符串获取WFS的属性值(xml为.xml格式的字符串)
	 * @param xml
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Map<String,String>> readWfsFeatureValue1(String xml){
		List<Map<String,String>> vals = new ArrayList<Map<String,String>>();
		Document doc = null;
		try{
			doc = DocumentHelper.parseText(xml); // 将字符串转为XML
			List<Element> lst = doc.selectNodes("//geoserver:seven_hotmap_test1");
			for (Element node : lst) {
				List<Element> fields = node.elements();
				Map<String,String> fieldMap = new HashMap<String,String>();
				for (Element element : fields) {
					fieldMap.put(element.getName(), element.getText());
				}
				vals.add(fieldMap);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return vals;
	}
	
	/**
	 * 根据WFS服务的XML字符串获取WFS有哪些属性
	 * @param xml
	 * @param type 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<String> readWfsFeatureType(String xml, Integer type) {
		List<String> valueFields = new ArrayList<String>();
		Document doc = null;
		try {
			// 读取并解析XML文档
			// SAXReader就是一个管道，用一个流的方式，把xml文件读出来
			// SAXReader reader = new SAXReader(); //User.hbm.xml表示你要解析的xml文档
			// Document document = reader.read(new File("User.hbm.xml"));
			// 下面的是通过解析xml字符串的
			doc = DocumentHelper.parseText(xml); // 将字符串转为XML
			List<Element> lst = doc.selectNodes("//xsd:element");
			for (Element element : lst) {
				String name = element.attributeValue("name");
				String fieldType = element.attributeValue("type").substring(4).toLowerCase();
				String substitutionGroup = element.attributeValue("substitutionGroup");
				if(name.equals("the_geom"))continue;
				if(substitutionGroup!=null)continue;
				if(type==1){
					valueFields.add(name);
				}else{
					if(fieldType.equals("double")){
						valueFields.add(name);
					}
				}
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return valueFields;
	}
	
	@SuppressWarnings("unchecked")
	public static List<Map<String, Object>> readWfsFeatureAttr(String xml,String featureType,String featureNs) {
		List<Map<String,Object>> attrLst = new ArrayList<Map<String,Object>>();
		Document doc = null;
		try {
			doc = DocumentHelper.parseText(xml); // 将字符串转为XML
			List<Element> lst = doc.selectNodes("//"+featureNs+":"+featureType);
			Long start = System.currentTimeMillis();
			System.out.println("开始时间："+start);
			for (Element element : lst) {
				Map<String,Object> attr = new HashMap<String,Object>();
				List<Element> eles = element.elements();
				Integer len1 = eles.size();
				for (int i = 0; i < len1; i++) {
					Element obj = eles.get(i);
					if(obj.getName().equals("the_geom")){
						Node geom = obj.selectSingleNode("//gml:coordinates");
						attr.put("the_geom", geom.getText());
					}else{
						attr.put(obj.getName(), obj.getText());
					}
				}
				Long end = System.currentTimeMillis();
				System.out.println("结束时间："+end);
				System.out.println("总耗时："+(end-start));
				attrLst.add(attr);
			}
		}catch(DocumentException e){
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return attrLst;
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, Object> readWfsLayerSource(String result) {
		Map<String,Object> layerSource = new HashMap<String,Object>();
		Document doc = null;
		try{
			doc = DocumentHelper.parseText(result); // 将字符串转为XML
			Element root = doc.getRootElement();
			List<Element> lst = root.elements();
			for (Element element : lst) {
				if(element.getName().equals("FeatureTypeList")){
					Element ft = (Element) element.elements().get(1);
					List<Element> eles = ft.elements();
					for (Element ele : eles) {
						if(ele.getName().equals("LatLongBoundingBox")){
							Map<String,String> qy = new HashMap<String,String>();
							qy.put("minx", ele.attributeValue("minx"));
							qy.put("miny", ele.attributeValue("miny"));
							qy.put("maxx", ele.attributeValue("maxx"));
							qy.put("maxy", ele.attributeValue("maxy"));
							layerSource.put(ele.getName(), qy);
						}else{
							layerSource.put(ele.getName(), ele.getText());
						}
					}
				}
			}
		}catch(DocumentException e){
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return layerSource;
	}
	
	public static void main(String[] args) {
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><xsd:schema xmlns:geoserver=\"geoserver\" xmlns:gml=\"http://www.opengis.net/gm\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" elementFormDefault=\"qualified\" targetNamespace=\"geoserver\">"
			+"<xsd:import namespace=\"http://www.opengis.net/gml\" schemaLocation=\"http://127.0.0.1:18888/geoserver/schemas/gml/2.1.2/feature.xsd\"/>"
		+"<xsd:complexType name=\"seven_hotmap_test1Type\">"
		  +"<xsd:complexContent>"
		    +"<xsd:extension base=\"gml:AbstractFeatureType\">"
		      +"<xsd:sequence>"
		        +"<xsd:element maxOccurs=\"1\" minOccurs=\"0\" name=\"the_geom\" nillable=\"true\" type=\"gml:MultiPolygonPropertyType\"/>"
		          +"<xsd:element maxOccurs=\"1\" minOccurs=\"0\" name=\"图幅号\" nillable=\"true\" type=\"xsd:string\"/>"
		          +"<xsd:element maxOccurs=\"1\" minOccurs=\"0\" name=\"实体编号\" nillable=\"true\" type=\"xsd:string\"/>"
		          +"<xsd:element maxOccurs=\"1\" minOccurs=\"0\" name=\"名称\" nillable=\"true\" type=\"xsd:string\"/>"
		          +"<xsd:element maxOccurs=\"1\" minOccurs=\"0\" name=\"地址\" nillable=\"true\" type=\"xsd:string\"/>"
		          +"<xsd:element maxOccurs=\"1\" minOccurs=\"0\" name=\"备注\" nillable=\"true\" type=\"xsd:string\"/>"
		          +"<xsd:element maxOccurs=\"1\" minOccurs=\"0\" name=\"照片\" nillable=\"true\" type=\"xsd:string\"/>"
		          +"<xsd:element maxOccurs=\"1\" minOccurs=\"0\" name=\"门牌号\" nillable=\"true\" type=\"xsd:string\"/>"
		          +"<xsd:element maxOccurs=\"1\" minOccurs=\"0\" name=\"栋号\" nillable=\"true\" type=\"xsd:string\"/>"
		          +"<xsd:element maxOccurs=\"1\" minOccurs=\"0\" name=\"唯一ID值\" nillable=\"true\" type=\"xsd:string\"/>"
		          +"<xsd:element maxOccurs=\"1\" minOccurs=\"0\" name=\"校区\" nillable=\"true\" type=\"xsd:string\"/>"
		          +"<xsd:element maxOccurs=\"1\" minOccurs=\"0\" name=\"面积\" nillable=\"true\" type=\"xsd:double\"/>"
		          +" </xsd:sequence>"
		        +"</xsd:extension>"
		      +"</xsd:complexContent>"
		    +" </xsd:complexType>"
		  +"<xsd:element name=\"seven_hotmap_test1\" substitutionGroup=\"gml:_Feature\" type=\"geoserver:seven_hotmap_test1Type\"/>"
		+"</xsd:schema>";
		List<String> lst = readWfsFeatureType(xml,1);
		System.out.println(lst.size());
	}
}