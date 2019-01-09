package com.imap.cloud.common.service.security;

import java.util.List;

import com.imap.cloud.common.entity.security.MethodClass;
import com.imap.cloud.common.service.base.BaseService;

public interface MethodClassService extends BaseService<MethodClass, String>{
	
	/**
	 * 删除自己
	 * 删除和权限的中间表
	 * @param id
	 * @throws Exception 
	 */
	void deleteByPkPermission(String id) throws Exception;
	
	/**
	 * @return 返回所有列表，返回格式JSON数组（只返回拦截的方法或类URL地址名称）
	 */
	List<MethodClass> findAllInterceptType();
	
	/**
	 * 方法或类修改时(有可能以被权限引用，在有修改拦截类型时，要对引用了的权限进行处理)
	 * @param methodClass 方法或类实体对象
	 * @throws Exception 
	 */
	void updateByPkPermission(MethodClass methodClass) throws Exception;
	
	/**
	 * 方法添加时(如添加的是不拦截的方法，则直接添加到初始化不拦截的map中)
	 * @param methodClass
	 */
	void insertPkPermission(MethodClass methodClass) throws Exception ;

}
