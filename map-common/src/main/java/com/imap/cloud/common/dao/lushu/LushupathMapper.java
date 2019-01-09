package com.imap.cloud.common.dao.lushu;

import com.imap.cloud.common.dao.base.BaseDao;
import com.imap.cloud.common.entity.lushu.Lushupath;

public interface LushupathMapper extends BaseDao<Lushupath, String> {
    int deleteByPrimaryKey(String id);

    int insert(Lushupath record);

    int insertSelective(Lushupath record);

    Lushupath selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Lushupath record);

    int updateByPrimaryKeyWithBLOBs(Lushupath record);

    int updateByPrimaryKey(Lushupath record);
    
    
}