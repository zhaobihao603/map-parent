package com.imap.cloud.common.dao.security;

import java.util.List;

import com.imap.cloud.common.dao.base.BaseDao;
import com.imap.cloud.common.entity.security.MethodClass;
import org.springframework.stereotype.Repository;

@Repository
public interface MethodClassMapper extends BaseDao<MethodClass, String>{
	
	/**
	 * 删除和权限的中间表
	 * @param id	方法或类ID
	 */
	void deleteByPkPermission(String id);
	
	/**
	 * @return	返回所有列表，返回格式JSON数组（只返回拦截的方法或类URL地址名称）
	 */
	List<MethodClass> findAllInterceptType();
	
	/**
	 * @return  返回不拦截的列表
	 */
	List<MethodClass> selectAllMethodClassIntercept();
	
	/**
	 * @return  返回方法或类URL引用的权限来查询可以访问的角色(角色权限)
	 */
	List<MethodClass> selectAllMethodClassInterceptRole();
	
	/**
	 * @return  返回方法或类URL引用的权限来查询可以访问的用户(个人权限)
	 */
	List<MethodClass> selectAllMethodClassInterceptUser();
	
	/**
	 * 查询当前方法或类是否被权限引用
	 * @param id	
	 * @return 如结果大于0，则以被使用
	 */
	int selectInspectQuotePermission(String id);

}
