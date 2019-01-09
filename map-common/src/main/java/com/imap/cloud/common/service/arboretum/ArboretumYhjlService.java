package com.imap.cloud.common.service.arboretum;

import java.util.List;

import com.imap.cloud.common.entity.arboretum.ArboretumYhjl;
import com.imap.cloud.common.service.base.BaseService;

/**
 * 养护记录 service
 * @author 冯林
 *
 */
public interface ArboretumYhjlService extends BaseService<ArboretumYhjl, String> {
	
	/**
	 * 返回该植被管理下的养护记录信息
	 * @param fid		植被管理ID
	 */
	List<ArboretumYhjl> selectFindZbglCuring(String fid);
	
}
