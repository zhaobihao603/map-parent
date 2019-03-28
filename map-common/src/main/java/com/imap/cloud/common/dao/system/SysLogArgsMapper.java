package com.imap.cloud.common.dao.system;

import com.imap.cloud.common.entity.system.SysLogArgs;
import org.springframework.stereotype.Repository;

@Repository
public interface SysLogArgsMapper {
    int deleteByPrimaryKey(String id);

    int insert(SysLogArgs record);

    int insertSelective(SysLogArgs record);

    SysLogArgs selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SysLogArgs record);

    int updateByPrimaryKey(SysLogArgs record);
}