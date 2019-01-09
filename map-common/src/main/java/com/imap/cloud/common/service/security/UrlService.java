package com.imap.cloud.common.service.security;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONArray;
import com.imap.cloud.common.dto.Pager;
import com.imap.cloud.common.entity.security.Url;
import com.imap.cloud.common.entity.system.SysFile;
import com.imap.cloud.common.service.base.BaseService;

/**
 * 资源访问url Service接口
 * @author 冯林
 *
 */
public interface UrlService extends BaseService<Url,String>{
	
	/**
	 * 
	 * @return 返回Url菜单中所有的父节点（后台）
	 */
	List<Map<String, Object>> findAllParentNode();
	
	/**
	 * 
	 * @return 返回Url菜单中所有的父节点（包括前后台）
	 */
	List<Map<String, Object>> findAllParentNodeAround();
	
	/**
	 * 
	 * @param list 当前用户的所有权限集合 (后台)
	 * @return	返回该用户的菜单Url
	 */
	//List<Url> selectPkPermissionUrl(List<String> list);
	List<Map<String,Object>> selectPkPermissionUrl(List<String> list);
	
	/**
	 * 删除URL
	 * 同时删除和权限中间表的数据
	 * @param id URL ID
	 * @throws Exception 
	 */
	void deleteByPkPermission(String id) throws Exception;
	
	/**
	 * 
	 * @param url
	 * @param pageNum
	 * @param pageSize
	 * @return 返回父节点
	 */
	Pager<Url> selectPageFather(Url url, Integer pageNum, Integer pageSize) throws Exception;
	
	/**
	 * @param id
	 * @return	返回子级菜点(和对应的父级菜单)
	 */
	Map<String, Object> selectUrlAndParentByPk(String id);
	
	/**
	 * 
	 * @return 返回所有菜单列表（子级菜单）只查询显示的。
	 */
	List<Url> findAllNoDispaly();
	
	/**
	 * @return 返回所有前端菜单列表(包括前台和后台)，返回格式JSON数组
	 */
	JSONArray findAllFrontParentNode(HttpServletRequest request);
	
	/**
	 * @return 返回当前排序中的最大值
	 */
	int findMaxOrder();
	
	/**
	 * (前台)查询子级或父级菜单中的code值，是否以存在，不存在返回 true, 存在返回false
	 * @param id 可以为null
	 * @param code
	 * @return
	 */
	boolean selectByPkCode(String id,String code);
	
	/**
	 * 重新加载角色对应的Url资源权限
	 */
	void getAllConfigAttributesCallRole() throws Exception;
	
	/**
	 * 重新加载用户对应的url资源权限
	 */
	void getAllConfigAttributesCallPermission() throws Exception;
	
	/**
	 * 加载所有不拦截的方法和类
	 * @throws Exception
	 */
	void getSelectAllNotMethodClassIntercept() throws Exception;
	
	/**
	 * 加载方法或类URL引用的权限来查询可以访问的角色(角色权限) 
	 * @throws Exception
	 */
	void getSelectAllMethodClassInterceptRole() throws Exception;
	
	/**
	 * 加载方法或类URL引用的权限来查询可以访问的用户(个人权限)
	 * @throws Exception
	 */
	void getSelectAllMethodClassInterceptUser() throws Exception;
	
	/**
	 * 上传图标
	 * @param request
	 * @return
	 */
	public SysFile uploadIcon(HttpServletRequest request);

	public List<Url> getByParentCode(String string)throws Exception;
}
