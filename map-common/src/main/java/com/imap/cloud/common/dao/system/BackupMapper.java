package com.imap.cloud.common.dao.system;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.imap.cloud.common.entity.system.Backup;
import org.springframework.stereotype.Repository;

@Repository
public interface BackupMapper {
	int insert(Backup backup);
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
	 * 根据id数组查询备份文件
	 * @param list
	 * @return
	 */
	List<Backup> selectByIds(String[] list);
}
