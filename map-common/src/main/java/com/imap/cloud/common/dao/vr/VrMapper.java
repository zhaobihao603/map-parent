package com.imap.cloud.common.dao.vr;

import org.apache.ibatis.annotations.Param;

import com.imap.cloud.common.dao.base.BaseDao;
import com.imap.cloud.common.entity.vr.Vr;

public interface VrMapper extends BaseDao<Vr, String> {
	//删除一条记录
    int deleteByPrimaryKey(String id);
    //新增一条记录
    int insert(Vr record);
    //查询一个记录
    Vr selectByPrimaryKey(String id);
    //建立VR上传连接
    int linkVrUpload(@Param(value="vrId") String vrId ,@Param(value="uploadId")String uploadId);
    
    int updateVrUpload(String vrId ,String uploadId);
    
    //删除上传连接关联
    int deleteVrUpload(@Param(value="vrId") String vrId);
    
    int updateByPrimaryKeySelective(Vr vr);
}