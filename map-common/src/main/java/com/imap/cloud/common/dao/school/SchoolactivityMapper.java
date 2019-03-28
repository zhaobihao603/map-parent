package com.imap.cloud.common.dao.school;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.imap.cloud.common.dao.base.BaseDao;
import com.imap.cloud.common.entity.school.Schoolactivity;
import com.imap.cloud.common.entity.vr.Vr;
import org.springframework.stereotype.Repository;

@Repository
public interface SchoolactivityMapper extends BaseDao<Schoolactivity, String> {
	
    int deleteByPrimaryKey(String id);

    int insert(Schoolactivity record);

    Schoolactivity selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Schoolactivity record);

    //查询所以信息
    List<Schoolactivity> selectAll();
    
    //模糊查询
    List<Schoolactivity> selectvague(String name);
    
    //发布
    int updateByfabu(@Param(value="id") String id,@Param(value="fabu") boolean fabu, @Param(value="fabuState") Integer fabuState);
    
    //建立与上传连接关联
    int linkschoolUpload(@Param(value="schoolId") String string ,@Param(value="uploadId")String ids);
    
    //修改关联ss
    int updateschoolUpload(@Param(value="schoolId") String schoolId ,@Param(value="uploadId")String ids);
    
    //删除与上传连接关联
    int deleteVrUpload(@Param(value="schoolId") String schoolId);
    
    
    
    
}