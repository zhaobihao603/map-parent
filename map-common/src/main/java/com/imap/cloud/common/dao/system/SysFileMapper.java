package com.imap.cloud.common.dao.system;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.imap.cloud.common.entity.system.SysFile;

public interface SysFileMapper {
	int save(SysFile file);
	int update(SysFile file);
	int updateById(SysFile file);
	int deleteById(String id);
	Long count();
	SysFile load(String id);
	List<SysFile> getByModule(String name);
	List<SysFile> select(Map conditionMap);
	List<SysFile> selectByIds(String[] list);
	/**
	 * 景点图片
	 * @param scenicId
	 */
	List<SysFile> selectScenicPhoto(String scenicId);
	List<SysFile> selectVrFiles(String id);
	List<SysFile> selectLushuImages(String id);
	List<SysFile> selectTeacherPhoto(String id);
	List<SysFile> selectActivityPhoto(String id);
}
