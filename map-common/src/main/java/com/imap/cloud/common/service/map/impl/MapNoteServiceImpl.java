package com.imap.cloud.common.service.map.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imap.cloud.common.dao.map.MapNoteMapper;
import com.imap.cloud.common.dao.scenic.ScenicMapper;
import com.imap.cloud.common.entity.map.MapNote;
import com.imap.cloud.common.service.base.impl.BaseServiceImpl;
import com.imap.cloud.common.service.map.MapNoteService;

@Service
public class MapNoteServiceImpl extends BaseServiceImpl<MapNote,String> implements MapNoteService {
	@Autowired
	private MapNoteMapper mapNoteMapper;
	
	@Autowired
	MapNoteServiceImpl(MapNoteMapper mapNoteMapper){
		this.mapNoteMapper =  mapNoteMapper;
		super.setBaseDao(mapNoteMapper);
	}
	
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Override
	public int deleteByPrimaryKey(String id) {
		return mapNoteMapper.deleteByPrimaryKey(id);
	}
	@Override
	public int insertSelective(MapNote entity) {
		Date currentTime = new Date();
		String dateString = formatter.format(currentTime);
		entity.setCreateTime(dateString);
		entity.setDeleted(false);
		
		return mapNoteMapper.insertSelective(entity);
	}
	@Override
	public MapNote selectByPrimaryKey(String id) {
		
		return mapNoteMapper.selectByPrimaryKey(id);
	}
	@Override
	public int updateByPrimaryKeySelective(MapNote record) {
		return mapNoteMapper.updateByPrimaryKeySelective(record);
	}
	@Override
	public int updateByPrimaryKey(MapNote record) {
		return mapNoteMapper.updateByPrimaryKey(record);
	}
	
	
	
}
