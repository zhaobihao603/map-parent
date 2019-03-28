package com.imap.cloud.common.dao.scenic;

import org.apache.ibatis.annotations.Param;

import com.imap.cloud.common.dao.base.BaseDao;
import com.imap.cloud.common.entity.scenic.Scenic;
import org.springframework.stereotype.Repository;

@Repository
public interface ScenicMapper extends BaseDao<Scenic, String>{
	
    int deleteByPrimaryKey(String id);

    int insert(Scenic record);

    Scenic selectByPrimaryKey(String id);

    int updateByPks(Scenic scenic);
    
    //建立与上传连接关联
    int linkScenicUpload(@Param(value="scenicid") String string ,@Param(value="uploadid")String ids);
    //删除与上传连接关联 
    int deleteScenicUpload(@Param(value="scenicid") String upid);
    //图片删除中间表关联
    int deleteUploadId(String uploadId);
    
    // 删除默认图片 
    //int deletePhotoId(@Param(value="id") int id);
    
    // 设置默认图片  
    int updatePhotoId(@Param(value="default_photo_id") String default_photo_id,@Param(value="id") String id);
    
}