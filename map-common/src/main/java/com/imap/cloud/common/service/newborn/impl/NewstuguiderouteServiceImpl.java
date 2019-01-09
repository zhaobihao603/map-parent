package com.imap.cloud.common.service.newborn.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imap.cloud.common.aop.annotation.LogDescription;
import com.imap.cloud.common.dao.newborn.NewstuguiderouteMapper;
import com.imap.cloud.common.dao.newborn.NewstuguidesiteMapper;
import com.imap.cloud.common.entity.newborn.Newstuguideroute;
import com.imap.cloud.common.entity.newborn.Newstuguidesite;
import com.imap.cloud.common.entity.newborn.UnitDto;
import com.imap.cloud.common.service.base.impl.BaseServiceImpl;
import com.imap.cloud.common.service.newborn.NewstuguiderouteService;
@LogDescription(name="新生指引服务")
@Service
public class NewstuguiderouteServiceImpl extends BaseServiceImpl< Newstuguideroute, String> implements NewstuguiderouteService  {
	
	@Autowired
	private NewstuguiderouteMapper newstuguideroutemapper;
	
	@Autowired
	private NewstuguidesiteMapper newstuguidesiteMapper;
	@Autowired
	NewstuguiderouteServiceImpl(NewstuguiderouteMapper newstuguideroutemapper){
		this.newstuguideroutemapper =  newstuguideroutemapper;
		super.setBaseDao(newstuguideroutemapper);
	}

	@Override
	@LogDescription(name="新生指引删除记录")
	@Transactional
	public int deleteByPrimaryKey(String id) {
		//删除外键关联
		newstuguidesiteMapper.deleteByroute(id);
		return newstuguideroutemapper.deleteByPrimaryKey(id);
	}

	@Override
	@LogDescription(name="新生指引插入记录")
	@Transactional
	public int inserts(Newstuguideroute route) {
		int num=0;
		try {
			route.setFabu(false);
			route.setFabuState(0);
			route.setAudited(false);
			route.setGuide_date(new Date());
			num=newstuguideroutemapper.insert(route);
		} catch (Exception e) {
			e.printStackTrace();
			num=0;
		}
		return num;
	}

	@Override
	@LogDescription(name="新生指引修改记录")
	@Transactional
	public int updateByPrimaryKey(Newstuguideroute record) {
		record.setFabu(true);
		record.setFabuState(0);
		record.setAudited(false);
		record.setGuide_date(new Date());
		return newstuguideroutemapper.updateByPrimaryKey(record);
	}

	@Override
	public List<Newstuguideroute> selectByPrimaryKey(String id) {
		return newstuguideroutemapper.selectByPrimaryKey(id);
	}

	@Override
	@LogDescription(name="新生指引修改发布记录")
	@Transactional
	public int updateByfabu(String id, boolean fabu) {
		return newstuguideroutemapper.updateByfabu(id, fabu);
	}

	@Override
	public List<Newstuguidesite> selectlist(String id) {
		return newstuguidesiteMapper.selectlist(id);
	}

	@Override
	public UnitDto selectNew(String id) {
		return newstuguideroutemapper.selectNew(id);
	}
}
