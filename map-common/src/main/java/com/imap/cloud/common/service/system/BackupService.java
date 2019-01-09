package com.imap.cloud.common.service.system;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.imap.cloud.common.dto.Pager;
import com.imap.cloud.common.entity.system.Backup;

/**
 * 数据库备份文件service层接口
 * @author 99901745
 *
 */
public interface BackupService {
	int save(Backup backup);
	int update(Backup backup);
	int updateById(Backup backup);
	int deleteById(String id);
	Long count();
	Backup load(String id);
	/**
	 * @param conditionMap
	 * @return
	 */
	List<Backup> select(Map conditionMap);
	
	/**
	 * 分页查询数据库备份文件信息
	 * @param conditionMap
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	Pager<Backup> pageAll(Map conditionMap, Integer pageNum, Integer pageSize);
}
