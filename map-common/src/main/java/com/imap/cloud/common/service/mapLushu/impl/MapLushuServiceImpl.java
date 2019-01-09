package com.imap.cloud.common.service.mapLushu.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imap.cloud.common.aop.annotation.LogDescription;
import com.imap.cloud.common.dao.mapLushu.MapLushuMapper;
import com.imap.cloud.common.entity.mapLushu.MapLushu;
import com.imap.cloud.common.service.base.impl.BaseServiceImpl;
import com.imap.cloud.common.service.mapLushu.MapLushuService;

@LogDescription(name="路书管理")
@Service
public class MapLushuServiceImpl extends BaseServiceImpl< MapLushu, String> implements MapLushuService {

	@Autowired 
	private MapLushuMapper mapLushuMapper;
	@Override
	public List<MapLushu> getBy(MapLushu entity) {
		try {
			List<MapLushu> list = mapLushuMapper.getBy(entity);
			
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	@Override
	public int insertItem(MapLushu entity) {
		int i =mapLushuMapper.insert(entity);
		return i;
	}
	@Override
	public int deleteBylushuIdAndTime(MapLushu entity) {
		int i =mapLushuMapper.deleteBylushuIdAndTime(entity);
		return i;
	}
	@Override
	public MapLushu getMapLushuById(String id) {
		MapLushu mapLushu = mapLushuMapper.selectByid(id);
		return mapLushu;
	}

	
}
