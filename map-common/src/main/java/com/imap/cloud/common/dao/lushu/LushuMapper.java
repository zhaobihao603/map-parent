package com.imap.cloud.common.dao.lushu;

import org.apache.ibatis.annotations.Param;

import com.imap.cloud.common.dao.base.BaseDao;
import com.imap.cloud.common.entity.lushu.Lushu;
import org.springframework.stereotype.Repository;

@Repository
public interface LushuMapper extends BaseDao<Lushu, String> {
	
    int deleteByPrimaryKey(String id);

    int insert(Lushu record);

    Lushu selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Lushu record);

    int updateByPrimaryKeyWithBLOBs(Lushu record);

    int updateByPrimaryKey(Lushu record);
    
    //发布
    int updateByfabu(@Param(value="id") String id,@Param(value="fabu") boolean fabu);
    
    //建立路书与上传连接
    int linkLushuUpload(@Param(value="lushuid") String lushuid ,@Param(value="uploadid")String uploadid);
    
    //修改路书与上传连接
    int updatelushuUpload(@Param(value="lushuid") String lushuid ,@Param(value="uploadid")String uploadid);
    
    //删除路书与上传连接
    int deleteLushuUpload(@Param(value="lushuid") String lushuid);

    //删除路书线路
	void deleteLushuPathsByLushuId(@Param(value="lushuid") String lushuid);    
}