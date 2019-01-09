package com.imap.cloud.common.service.map;

import java.util.List;

import com.imap.cloud.common.entity.map.MapDir;
import com.imap.cloud.common.service.base.BaseService;

/**
 * 
 * @author Hxin
 *
 */
public interface MapDirService extends BaseService<MapDir,String>{
	
	public List<MapDir> listByParentId(String id,Boolean enabled) throws Exception;

	public int countByParentId(String parentId);

}
