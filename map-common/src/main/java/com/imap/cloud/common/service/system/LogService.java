package com.imap.cloud.common.service.system;


import java.sql.Timestamp;

import org.aspectj.lang.ProceedingJoinPoint;

import com.imap.cloud.common.dto.Pager;
import com.imap.cloud.common.entity.system.SysLog;

public abstract interface LogService {
	public abstract SysLog createRecord(SysLog entity);

	public abstract SysLog createRecord(SysLog entity, String paramString);

	public abstract String getHandler();

	public abstract String getHandler(ProceedingJoinPoint paramProceedingJoinPoint);

//	public abstract Page<SysLog> pageRecord(String paramString,
//			Pagination paramPagination);

	public SysLog loadRecord(String uuid);
	
	public Pager<SysLog> pageRecord(String username,Timestamp startTime, Timestamp endTime, Integer pageNum,Integer pageSize);
	
	/**
	 * 保存系统的操作日志
	 * @param syslog
	 * @return
	 */
	int save(SysLog syslog);
	
	
	
}
