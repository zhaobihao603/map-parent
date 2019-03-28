package com.imap.cloud.common.dao.system;

import com.imap.cloud.common.entity.system.TrackLog;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Repository
public interface TrackLogMapper {
    int deleteById(String id);
    /**
     * 全部插入，主键id为自动生成uuid（32位）
     * @param record
     * @return
     */
    int insert(TrackLog record);
    /**
     * 没有属性值的不作插入操作，主键id为需要手动指定
     * @param record
     * @return
     */
    int insertSelective(TrackLog record);

    TrackLog selectById(String id);
    /**
     * 没有属性值的不作更新操作
     * @param record
     * @return
     */
    int updateByIdSelective(TrackLog record);

    int updateById(TrackLog record);

    TrackLog getLatestLog(@Param("sessionId") String sessionId, @Param("ip") String ip);
    
    Integer getPV(@Param("startTime")Timestamp startTime,@Param("endTime") Timestamp endTime);
    
    Integer getUV(@Param("startTime")Timestamp startTime,@Param("endTime") Timestamp endTime);
	
    void addTime(@Param("time") Integer time);
    
	List<Map<String,Object>> countByIP(@Param("startTime")Timestamp startTime,@Param("endTime") Timestamp endTime);
	
	List<Map<String,Object>> countByPage(@Param("startTime")Timestamp startTime,@Param("endTime") Timestamp endTime);
	
	List<Map<String,Object>> countByDate(@Param("startTime")Timestamp startTime,@Param("endTime") Timestamp endTime);
	
	List<TrackLog> selectAll(@Param("startTime")Timestamp startTime,@Param("endTime") Timestamp endTime);
}