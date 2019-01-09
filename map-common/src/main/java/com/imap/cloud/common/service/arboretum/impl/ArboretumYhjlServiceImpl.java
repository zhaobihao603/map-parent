package com.imap.cloud.common.service.arboretum.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imap.cloud.common.dao.arboretum.ArboretumYhjlMapper;
import com.imap.cloud.common.entity.arboretum.ArboretumYhjl;
import com.imap.cloud.common.service.arboretum.ArboretumYhjlService;
import com.imap.cloud.common.service.base.impl.BaseServiceImpl;

@Service("arboretumYhjlServiceIpml")
public class ArboretumYhjlServiceImpl extends BaseServiceImpl<ArboretumYhjl, String> implements ArboretumYhjlService{

	private ArboretumYhjlMapper arboretumYhjlMapper;
	
	@Autowired
	ArboretumYhjlServiceImpl(ArboretumYhjlMapper arboretumYhjlMapper){
		this.arboretumYhjlMapper = arboretumYhjlMapper;
		super.setBaseDao(arboretumYhjlMapper);
	}

	public List<ArboretumYhjl> selectFindZbglCuring(String fid) {
		return arboretumYhjlMapper.selectFindZbglCuring(fid);
	}
}
