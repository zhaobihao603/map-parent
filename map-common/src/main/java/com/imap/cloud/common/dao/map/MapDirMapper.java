package com.imap.cloud.common.dao.map;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.imap.cloud.common.dao.base.BaseDao;
import com.imap.cloud.common.entity.map.MapDir;

public interface MapDirMapper extends BaseDao<MapDir, String>{

	List<MapDir> listByParentId(@Param("parentId")String parentId,@Param("enabled")Boolean enabled);

	int countByParentId(String parentId);
	
}