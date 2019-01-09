package com.imap.cloud.common.dao.system;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.imap.cloud.common.entity.system.SysLog;

public interface SysLogMapper {
    int deleteByPrimaryKey(String id);

    int insert(SysLog record);

    int insertSelective(SysLog record);

    SysLog selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SysLog record);

    int updateByPrimaryKey(SysLog record);

	List<SysLog> pageRecord(@Param("username")String username,@Param("startTime")Date startTime,@Param("endTime")Date endTime);
}