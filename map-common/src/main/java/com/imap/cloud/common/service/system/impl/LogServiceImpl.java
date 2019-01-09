package com.imap.cloud.common.service.system.impl;


import java.sql.Timestamp;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.imap.cloud.common.aop.annotation.LogDescription;
import com.imap.cloud.common.dao.system.SysLogArgsMapper;
import com.imap.cloud.common.dao.system.SysLogMapper;
import com.imap.cloud.common.dto.Pager;
import com.imap.cloud.common.entity.system.SysLog;
import com.imap.cloud.common.entity.system.TrackLog;
import com.imap.cloud.common.service.system.LogService;
import com.imap.cloud.common.util.AccountUtils;

@LogDescription(name="操作日志的服务")
@Service
public class LogServiceImpl implements LogService {
	@Autowired
	private SysLogMapper sysLogMapper;
	
	@Autowired
	private SysLogArgsMapper sysLogArgsMapper;


	@LogDescription(name="创建操作日志")
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public SysLog createRecord(SysLog record) {
		record.setId(AccountUtils.getUUID());
		this.sysLogMapper.insert(record);
		return record;
	}

	@LogDescription(name="创建操作日志")
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public SysLog createRecord(SysLog record,String handler) {
		record.setUsername(handler);
		record.setId(AccountUtils.getUUID());
		this.sysLogMapper.insert(record);
		return record;
	}

	public String getHandler() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			return auth.getName();
		}
		return null;
	}

	public String getHandler(ProceedingJoinPoint jp) {
		return getHandler();
	}
	
	@LogDescription(name="分页获取操作日志")
	@Transactional(readOnly = true)
	public Pager<SysLog> pageRecord(String username,Timestamp startTime, Timestamp endTime, 
			Integer pageNum,Integer pageSize){
		Page<TrackLog> page = PageHelper.startPage(pageNum==null?1:pageNum,pageSize==null?10:pageSize,true);
		List<SysLog> pageList = this.sysLogMapper.pageRecord(username,startTime,endTime);
		return new Pager(pageList);
	}

//	@Transactional(readOnly = true)
//	public Page<SysLog> pageRecord(String username,
//			Pagination pageArg) {
//		Page page = null;
//		if (StringUtils.isNotBlank(username))
//			page = this.sysLogMapper.pageAll(pageArg);
//		else {
//			page = this.sysLogMapper.pageByUser("%" + username + "%",
//					pageArg);
//		}
//		return page;
//	}

	@LogDescription(name="获取单条操作日志记录")
	@Transactional(readOnly = true)
	public SysLog loadRecord(String uuid) {
		return null;//(SysLog) this.sysLogMapper.read(uuid);
	}

	@LogDescription(name="保存操作日志")
	@Override
	@Transactional
	public int save(SysLog syslog) {
		syslog.setId(AccountUtils.getUUID());
		return this.sysLogMapper.insert(syslog);
	}
	
}
