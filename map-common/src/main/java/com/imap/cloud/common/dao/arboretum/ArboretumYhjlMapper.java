package com.imap.cloud.common.dao.arboretum;

import java.util.List;

import com.imap.cloud.common.dao.base.BaseDao;
import com.imap.cloud.common.entity.arboretum.ArboretumYhjl;
import org.springframework.stereotype.Repository;

/**
 * 养护记录DAO
 * @author 冯林
 *
 */
@Repository
public interface ArboretumYhjlMapper extends BaseDao<ArboretumYhjl, String>{
	
	/**
	 * 返回该植被管理下的养护记录信息
	 * @param fid		植被管理ID
	 */
	List<ArboretumYhjl> selectFindZbglCuring(String fid);
	
}
