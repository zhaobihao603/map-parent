package com.imap.cloud.common.service.security.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imap.cloud.common.aop.annotation.LogDescription;
import com.imap.cloud.common.dao.security.MethodClassMapper;
import com.imap.cloud.common.entity.security.MethodClass;
import com.imap.cloud.common.service.base.impl.BaseServiceImpl;
import com.imap.cloud.common.service.security.MethodClassService;
import com.imap.cloud.common.service.security.UrlService;

@LogDescription(name="方法和类URL拦截资源管理")
@Service("methodClassServiceIpml")
public class MethodClassServiceImpl extends BaseServiceImpl<MethodClass, String> implements MethodClassService{

	private MethodClassMapper methodClassMapper;
	@Resource
	private UrlService urlService;
	
	@Autowired
	MethodClassServiceImpl(MethodClassMapper methodClassMapper){
		this.methodClassMapper = methodClassMapper;
		super.setBaseDao(methodClassMapper);
	}
	
	@Transactional
	@LogDescription(name="删除方法或类URL拦截资源")
	public void deleteByPkPermission(String id) throws Exception {
		int inspectQuotePermission = methodClassMapper.selectInspectQuotePermission(id);
		super.deleteByPk(id);
		//判断该方法或类URL是否被权限引用
		if(inspectQuotePermission > 0){
			methodClassMapper.deleteByPkPermission(id);
			urlService.getSelectAllMethodClassInterceptUser();
			urlService.getSelectAllMethodClassInterceptRole();
		}
	}

	public List<MethodClass> findAllInterceptType() {
		return methodClassMapper.findAllInterceptType();
	}
	
	@Transactional
	@LogDescription(name="修改方法或类URL拦截资源")
	public void updateByPkPermission(MethodClass methodClass) throws Exception {
		MethodClass obj = methodClassMapper.selectByPk(methodClass.getId());
		super.updateByPk(methodClass);
		boolean open = true;
		//修改了拦截状态
		if(obj.getInterceptType().equals(methodClass.getInterceptType())){
			if(methodClass.getInterceptType().equals("0")){         // 1 = 0  不拦截 修改为 拦截 
				urlService.getSelectAllNotMethodClassIntercept();
			}else{													// 0 = 1 拦截 修改为 不拦截 
				urlService.getSelectAllNotMethodClassIntercept();
				urlService.getSelectAllMethodClassInterceptUser();
				urlService.getSelectAllMethodClassInterceptRole();
				open = false;
			}
		}
		//修改了类型  方法  / 类
		if(obj.getType().equals(methodClass.getType())){
			if(open){
				urlService.getSelectAllMethodClassInterceptUser();
				urlService.getSelectAllMethodClassInterceptRole();
			}
		}
	}
	
	@Transactional
	@LogDescription(name="添加方法或类URL拦截资源")
	public void insertPkPermission(MethodClass methodClass) throws Exception  {
		super.insert(methodClass);
		if(methodClass.getInterceptType().equals("1")){
			urlService.getSelectAllNotMethodClassIntercept();
		}
	}
}
