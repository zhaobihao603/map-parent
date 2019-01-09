package com.imap.cloud.common.service.map.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imap.cloud.common.dao.map.MapDirMapper;
import com.imap.cloud.common.entity.map.MapDir;
import com.imap.cloud.common.service.base.impl.BaseServiceImpl;
import com.imap.cloud.common.service.map.MapDirService;

@Service
public class MapDirServiceImpl extends BaseServiceImpl<MapDir,String> implements MapDirService {
	@Autowired
	private MapDirMapper mapDirMapper;
	//这句必须要加上。不然会报空指针异常，因为在实际掉用的时候不是BaseDao调用，而是这个mapDirMapper
	@Autowired
	public void setBaseMapper(){
		super.setBaseDao(mapDirMapper);
	}
	@Override
	public List<MapDir> listByParentId(String parentId,Boolean enabled) throws Exception {
		return mapDirMapper.listByParentId(parentId,enabled);
	}
	
	@Override
	public int countByParentId(String parentId){
		return mapDirMapper.countByParentId(parentId);
	}
	
}
