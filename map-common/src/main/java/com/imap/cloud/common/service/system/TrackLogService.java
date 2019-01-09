package com.imap.cloud.common.service.system;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.imap.cloud.common.dto.Pager;
import com.imap.cloud.common.entity.system.TrackLog;

public interface TrackLogService {
	
	public TrackLog getLatestLog(String id, String ip);

	public void update(String id, long stayTime);

	public void insert(TrackLog log);
	
	public int getPV(Timestamp startTime, Timestamp endTime);

	public int getUV(Timestamp startTime, Timestamp endTime);

	public List<Map<String,Object>> countByIP(Timestamp startTime, Timestamp endTime);
	
	public List<Map<String,Object>> countByPage(Timestamp startTime, Timestamp endTime);

	public Pager<TrackLog> selectAll(Timestamp startTime, Timestamp endTime,
			Integer pageNum, Integer pageSize);

	public List<Map<String, Object>> countByDate(Timestamp start, Timestamp end);
	
}
