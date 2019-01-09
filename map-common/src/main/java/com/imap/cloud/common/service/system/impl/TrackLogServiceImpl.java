package com.imap.cloud.common.service.system.impl;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.util.StringUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.imap.cloud.common.aop.annotation.LogDescription;
import com.imap.cloud.common.cache.RedisCache;
import com.imap.cloud.common.dao.system.TrackLogMapper;
import com.imap.cloud.common.dto.Pager;
import com.imap.cloud.common.entity.system.TrackLog;
import com.imap.cloud.common.service.system.TrackLogService;
import com.imap.cloud.common.util.AccountUtils;

@LogDescription(name="访问日志的服务")
@Service(value="trackLogService")
public class TrackLogServiceImpl implements TrackLogService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private TrackLogMapper trackLogMapper;
	@Autowired
	private RedisCache cache;
	
	@LogDescription(name="获取最新的访问日志")
	public TrackLog getLatestLog(String sessionId, String ip) {
		TrackLog log = trackLogMapper.getLatestLog(sessionId, ip);
		return log;
	}

	@LogDescription(name="保存访问日志")
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void insert(TrackLog record) {
		record.setId(AccountUtils.getUUID());
		trackLogMapper.insert(record);
	}

	@LogDescription(name="更新访问日志的停留时间")
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void update(String id, long stayTime) {
		logger.info("update|"+id+"|"+stayTime);
		if(!StringUtils.isEmpty(id)){
			TrackLog record = new TrackLog();
			record.setId(id);
			record.setStayTime((int) stayTime);
			trackLogMapper.updateByIdSelective(record);
		}
	}

	@LogDescription(name="获取某时间段的PV")
	public int getPV(Timestamp startTime, Timestamp endTime) {
		Integer pv = trackLogMapper.getPV(startTime, endTime);
		return pv==null?0:pv;
	}

	@LogDescription(name="获取某时间段的UV")
	public int getUV(Timestamp startTime, Timestamp endTime) {
		Integer uv = trackLogMapper.getUV(startTime, endTime);
		return uv==null?0:uv;
	}

	@LogDescription(name="根据IP获取某时间段的访问日志")
	@Override
	public List<Map<String,Object>> countByIP(Timestamp startTime, Timestamp endTime) {
		List<Map<String,Object>> list = trackLogMapper.countByIP(startTime, endTime);
		return list;
	}

	@LogDescription(name="根据访问页面获取某时间段的访问日志")
	@Override
	public List<Map<String,Object>> countByPage(Timestamp startTime, Timestamp endTime) {
		List<Map<String,Object>> list = trackLogMapper.countByPage(startTime, endTime);
		return list;
	}
	
	@LogDescription(name="获取某时间段的访问日志")
	@Override
	public List<Map<String, Object>> countByDate(Timestamp startTime, Timestamp endTime){
		List<Map<String,Object>> list = trackLogMapper.countByDate(startTime, endTime);
		return list;
	}
	
	@SuppressWarnings({ "unused", "rawtypes", "unchecked" })
	@LogDescription(name="根据条件获取访问日志")
	public Pager<TrackLog> selectAll(Timestamp startTime, Timestamp endTime,
			Integer pageNum, Integer pageSize){
		Page<TrackLog> page = PageHelper.startPage(pageNum==null?1:pageNum,pageSize==null?10:pageSize,true);
        List<TrackLog> pageList = trackLogMapper.selectAll(startTime, endTime);
		//可以用PageInfo对结果进行包装，PageInfo包装过度，可以用本项目的Pager
		return new Pager(pageList);
	}

}
