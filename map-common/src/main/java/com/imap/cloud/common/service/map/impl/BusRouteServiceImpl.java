package com.imap.cloud.common.service.map.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.imap.cloud.common.aop.annotation.LogDescription;
import com.imap.cloud.common.dao.map.BusRouteMapper;
import com.imap.cloud.common.dto.Pager;
import com.imap.cloud.common.entity.map.BusRoute;
import com.imap.cloud.common.service.map.BusRouteService;
@LogDescription(name="校巴服务")
@Service
public class BusRouteServiceImpl implements BusRouteService {
	
	@Autowired
	private BusRouteMapper busRouteMapper;
	
	@LogDescription(name="校巴获取")
	@Override
	public List<BusRoute> getBusRoutes(Map conditionMap) {
		return busRouteMapper.getBusRoutes(conditionMap);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@LogDescription(name="校巴获取")
	@Override
	public Pager<BusRoute> pageBusRoutes(Map conditionMap, Integer pageSize,
			Integer pageNum) {
		//PageHelper只对紧跟着的第一个SQL语句起作用
		Page<BusRoute> p = PageHelper.startPage(pageNum==null?1:pageNum,pageSize==null?10:pageSize,true);
		List<BusRoute> routes = busRouteMapper.getBusRoutes(conditionMap);
		return new Pager(routes,p);
	}

	
	@LogDescription(name="校巴保存")
	@Override
	public int insert(BusRoute busRoute) {
		return busRouteMapper.insert(busRoute);
	}

	@LogDescription(name="校巴更新")
	@Override
	public int update(BusRoute busRoute) {
		return busRouteMapper.update(busRoute);
	}

	@LogDescription(name="校巴删除")
	@Override
	public int delete(String id) {
		return busRouteMapper.delete(id);
	}
}
