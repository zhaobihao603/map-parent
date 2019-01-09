package com.imap.cloud.common.service.scenic;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.formula.functions.T;

import com.imap.cloud.common.dto.BaseResult;
import com.imap.cloud.common.entity.scenic.Scenic;
import com.imap.cloud.common.service.base.BaseService;

public interface ScenicService  extends BaseService<Scenic,String> {
	
	int deleteByPrimaryKey(String id);
	
	int deleteUploadId(String uploadId);
	
    Scenic selectByPrimaryKey(String id);

    int inserts(Scenic scenic);
    
    int updateByPks(Scenic scenic);
    
    // 设置默认图片  
    int updatePhotoId(String default_photo_id,String id);
    
    /**
     * 上传景点图片
     * @param id，景点id
     * @param request
     * @return
     */
    int uploadScenicPic(String id,String title,String desc,HttpServletRequest request);
    /**
     * 1、如果有文件,上传文件， 删除原有的关联 ，建立新的上传文件关联
     * 2、没有文件上传，更改其它信息 原关联表数据不变
     * @param id，景点id
     * @param request
     * @return
     */
	public BaseResult<T> updatePhoto(String id,String uploadId,String title,String desc,HttpServletRequest request);
}
