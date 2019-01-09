package com.imap.cloud.common.service.map.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.imap.cloud.common.aop.annotation.LogDescription;
import com.imap.cloud.common.dao.system.BackupMapper;
import com.imap.cloud.common.dto.Pager;
import com.imap.cloud.common.entity.system.Backup;
import com.imap.cloud.common.service.system.BackupService;
@LogDescription(name="数据库备份")
@Service
public class BackupServiceImpl implements BackupService {

	@Autowired
	private BackupMapper backupMapper;
	
	@LogDescription(name="数据库备份信息保存")
	@Override
	public int save(Backup backup) {
		return backupMapper.save(backup);
	}

	@LogDescription(name="数据库备份信息更新")
	@Override
	public int update(Backup backup) {
		return backupMapper.update(backup);
	}

	@LogDescription(name="数据库备份信息更新")
	@Override
	public int updateById(Backup backup) {
		return backupMapper.updateById(backup);
	}

	@LogDescription(name="数据库备份信息删除")
	@Override
	public int deleteById(String id) {
		return backupMapper.deleteById(id);
	}

	@LogDescription(name="数据库备份信息统计")
	@Override
	public Long count() {
		return backupMapper.count();
	}

	@LogDescription(name="数据库备份信息加载")
	@Override
	public Backup load(String id) {
		return backupMapper.load(id);
	}

	@LogDescription(name="数据库备份信息查询")
	@Override
	public List<Backup> select(Map conditionMap) {
		return backupMapper.select(conditionMap);
	}

	@Override
	public Pager<Backup> pageAll(Map conditionMap, Integer pageNum,
			Integer pageSize) {
		Page<Backup> p = PageHelper.startPage(pageNum==null?1:pageNum,pageSize==null?10:pageSize,true);
		List<Backup> lst = backupMapper.select(conditionMap);
		return new Pager<Backup>(lst,p);
	}
}
