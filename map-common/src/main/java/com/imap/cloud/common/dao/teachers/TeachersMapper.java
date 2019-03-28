package com.imap.cloud.common.dao.teachers;

import com.imap.cloud.common.dao.base.BaseDao;
import com.imap.cloud.common.entity.teachers.Teachers;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeachersMapper extends BaseDao<Teachers, String>  {
	//删除一条记录
    int deleteByPrimaryKey(String id);
    //新增一条记录
    int insert(Teachers teachers);
    
    int updateByPrimaryKeySelective(Teachers teachers);
    
    //查询一个记录
    Teachers selectByPrimaryKey(String id);
    //建立上传连接关联
    int linkTeachersUpload(@Param(value="teachersId") String teachersId ,@Param(value="uploadId")String ids);
    //删除上传连接关联
    int deleteTeachersUpload(String upid);
    
    //更改与上传连接关联
    int updateteachersUpload(@Param(value="teachersId") String teachersId ,@Param(value="uploadId")String uploadId);
    
    //根据校区查询所有实验室
    List<Teachers> selectLaboratory(String area);
}