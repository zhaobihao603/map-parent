package com.imap.cloud.common.service.lushu.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imap.cloud.common.aop.annotation.LogDescription;
import com.imap.cloud.common.dao.lushu.LushupathMapper;
import com.imap.cloud.common.entity.lushu.Lushupath;
import com.imap.cloud.common.service.base.impl.BaseServiceImpl;
import com.imap.cloud.common.service.lushu.LushupathService;

@LogDescription(name="路书服务")
@Service
public class LushupathServiceImpl extends BaseServiceImpl< Lushupath, String> implements LushupathService{
	
	@Autowired
	private LushupathMapper lushupathMapper;
	
	@Autowired
	LushupathServiceImpl(LushupathMapper lushupathMapper){
		this.lushupathMapper =  lushupathMapper;
		super.setBaseDao(lushupathMapper);
	}

	@Override
	public Lushupath selectByPrimaryKey(String id) {
		
		return lushupathMapper.selectByPrimaryKey(id);
		
	}

	@Override
	@LogDescription(name="路书删除记录")
	@Transactional
	public int deleteByPrimaryKey(String id) {
		
		return lushupathMapper.deleteByPrimaryKey(id);
	}

	@Override
	@LogDescription(name="路书插入记录")
	@Transactional
	public int insertSelective(Lushupath record) {
	
		return lushupathMapper.insertSelective(record);
	}

	@Override
	@LogDescription(name="路书修改记录")
	@Transactional
	public int updateByPrimaryKeySelective(Lushupath record) {
		
		return lushupathMapper.updateByPrimaryKeySelective(record);
	}
}
