package com.imap.cloud.common.service.map.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imap.cloud.common.dao.map.MapLayerRenderMapper;
import com.imap.cloud.common.entity.map.MapLayerRender;
import com.imap.cloud.common.service.map.MapLayerRenderService;

@Service
public class MapLayerRenderServiceImpl implements MapLayerRenderService {
	@Autowired
	private MapLayerRenderMapper mapLayerRenderMapper;

	@Override
	public MapLayerRender selectByPk(String layerId, String showId,String valueFields) {
		return mapLayerRenderMapper.selectByPk(layerId, showId, valueFields);
	}

	@Override
	public Integer insert(MapLayerRender mapLayerRender) {
		return mapLayerRenderMapper.insert(mapLayerRender);
	}

	@Override
	public Integer updateByPkSelective(MapLayerRender mapLayerRender) {
		return mapLayerRenderMapper.updateByPkSelective(mapLayerRender);
	}

	@Override
	public Integer deleteByPk(String layerId, String showId) {
		return mapLayerRenderMapper.deleteByPk(layerId, showId);
	}

	@Override
	public List<String> getCusFeatureType(String usedDatabaseName,List<String> columnsType) {
		return mapLayerRenderMapper.selectColumns(usedDatabaseName,columnsType);
	}

	@Override
	public List<Map<String,String>> selectByValueFields(String layerId, String[] valueFields) {
		List<Map<String,String>> lst = mapLayerRenderMapper.selectByValueFields(layerId,valueFields);
		List<Map<String,String>> result = new ArrayList<Map<String,String>>();
		for (Map<String, String> map : lst) {
			Map<String,String> res = new HashMap<String,String>();
			res.put("count", map.get("map"));
			String key = "";
			String value = "";
			for (int i = 0; i < valueFields.length; i++) {
				if(i>0){
					key+=","+valueFields[i];
					value+=","+map.get(valueFields[i]);
				}else{
					key+=valueFields[i];
					String val = String.valueOf(map.get(valueFields[i]));
					value+=val;
				}
			}
			res.put(key, value);
			result.add(res);
		}
		return result;
	}

	@Override
	public List<Map<String, String>> selectByDBValue(String layerId,Integer classes,
			String[] valueFields) {
		List<Map<String,String>> grade = new ArrayList<Map<String,String>>();
		List<Double> gradeVals = new ArrayList<Double>();
		List<Map<String,String>> vals = mapLayerRenderMapper.selectByValueFields(layerId,valueFields);
		for (Map<String, String> map : vals) {
			gradeVals.add(Double.parseDouble(String.valueOf(map.get(valueFields[0]))));
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
}
