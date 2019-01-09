package com.imap.cloud.common.service.newborn.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imap.cloud.common.aop.annotation.LogDescription;
import com.imap.cloud.common.dao.newborn.NewstuguidesiteMapper;
import com.imap.cloud.common.entity.newborn.Newstuguideroute;
import com.imap.cloud.common.entity.newborn.Newstuguidesite;
import com.imap.cloud.common.service.base.impl.BaseServiceImpl;
import com.imap.cloud.common.service.newborn.NewstuguidesiteService;

@LogDescription(name="新生指引服务")
@Service
public class NewstuguidesiteServiceImpl extends BaseServiceImpl< Newstuguidesite, String> implements NewstuguidesiteService {
	
	@Autowired
	private NewstuguidesiteMapper newstuguidesitemapper;
	
	@Autowired
	NewstuguidesiteServiceImpl(NewstuguidesiteMapper newstuguidesitemapper){
		this.newstuguidesitemapper =  newstuguidesitemapper;
		super.setBaseDao(newstuguidesitemapper);
	}

	@Override
	@LogDescription(name="新生指引删除记录")
	@Transactional
	public int deleteByroute(String id) {
		return newstuguidesitemapper.deleteByroute(id);
	}

	@Override
	@LogDescription(name="新生指引插入记录")
	@Transactional
	public int inserts(Newstuguidesite route) {
		return newstuguidesitemapper.inserts(route);
	}

	@Override
	@LogDescription(name="新生指引修改记录")
	@Transactional
	public int updateByPrimaryKey(Newstuguidesite record) {
		return newstuguidesitemapper.updateByPrimaryKey(record);
	}

	@Override
	public Newstuguidesite selectByPrimaryKey(String id) {
		
		return newstuguidesitemapper.selectByPrimaryKey(id);
	}

	@Override
	@LogDescription(name="新生指引删除记录")
	@Transactional
	public int deleteByPrimaryKey(String id) {
		return newstuguidesitemapper.deleteByPrimaryKey(id);
	}

}
