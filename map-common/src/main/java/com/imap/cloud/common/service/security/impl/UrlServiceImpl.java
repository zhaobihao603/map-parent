package com.imap.cloud.common.service.security.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.imap.cloud.common.aop.annotation.LogDescription;
import com.imap.cloud.common.dao.security.MethodClassMapper;
import com.imap.cloud.common.dao.security.UrlMapper;
import com.imap.cloud.common.dto.Pager;
import com.imap.cloud.common.entity.security.MethodClass;
import com.imap.cloud.common.entity.security.Permission;
import com.imap.cloud.common.entity.security.Role;
import com.imap.cloud.common.entity.security.Url;
import com.imap.cloud.common.entity.security.User;
import com.imap.cloud.common.entity.system.SysFile;
import com.imap.cloud.common.enums.Module;
import com.imap.cloud.common.service.base.impl.BaseServiceImpl;
import com.imap.cloud.common.service.security.UrlService;
import com.imap.cloud.common.service.system.FileService;
import com.imap.cloud.common.util.AccountUtils;

/**
 * 资源URL实现类 根据URL地址,查询当前URL可以访问的角色集合 url-->permission-->role
 * FilterInvocationSecurityMetadataSource是SecurityMetadataSource的子类
 * 
 * @author 冯林
 * 
 */
@LogDescription(name="菜单URL资源管理、URL访问拦截处理")
@Service(value = "urlService")
@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
public class UrlServiceImpl extends BaseServiceImpl<Url, String> implements UrlService, FilterInvocationSecurityMetadataSource {

	private UrlMapper urlMapper;
	@Autowired
	private MethodClassMapper methodClassMapper;
	
	@Resource
	private FileService fileService;

	private final Map<String, Set<Permission>> urlMap = new HashMap<String, Set<Permission>>(); //urlMap用于存储 URL与 Role集合的映射关系 
	private final Map<String, Set<Permission>> userMap = new HashMap<String, Set<Permission>>();//userMap用于存储URL与用户集合的关系
	private final Collection<ConfigAttribute> returnUrl = new HashSet<ConfigAttribute>();		//returnUrl用于 return跳转到 RoleAccessDecisionManager
	private final Map<String, String> MCNotInterceptTypeMethodMap = new HashMap<>();			//存储不拦截的方法
	private final Map<String, String> MCNotInterceptTypeClassMap = new HashMap<>();				//存储不拦截的类
	private final Map<String, Set<Permission>> MCInInterceptTypeMethodMapRole = new HashMap<>();//存储拦截的方法对应的角色权限
	private final Map<String, Set<Permission>> MCInInterceptTypeMethodMapMy = new HashMap<>();	//存储拦截的方法对应的个人权限
	private final Map<String, Set<Permission>> MCInInterceptTypeClassMapRole = new HashMap<>();	//存储拦截的类对应的角色权限
	private final Map<String, Set<Permission>> MCInInterceptTypeClassMapMy = new HashMap<>();	//存储拦截的类对应的个人权限
	
	private Set<Permission> USet;			//USet用于存储权限与用户的关系 
	private Set<Permission> RSet;			//RSet用于存储角色与用户的关系 
	private Set<Permission> MethodMySet;
	private Set<Permission> MethodRoleSet;
	private Set<Permission> ClassMySet;
	private Set<Permission> ClassRoleSet;
	
	private final String HTML = "HTML";
	private final String JSP = "JSP";
	private final String html = "html";
	private final String jsp = "jsp";
	private final String intercept = ".intercept";
	
	@Autowired
	UrlServiceImpl(UrlMapper urlMapper) {
		super.setBaseDao(urlMapper);
		this.urlMapper = urlMapper;
	}

	{
		Role role = new Role();
		returnUrl.add(role);
	}

	/**
	 * URL资源拦截核心方法
	 * 
	 * 如访问的是前端页面，请在spring-security.xml中配置该页面，则不进行该页面的拦截 例： <!-- 该路径下不需要过渡 -->
	 * <http pattern="/templet/login.html" security="none"></http>
	 * 如访问方法的URL带有 .intercept 则直接放行
	 * 
	 * @param object
	 *            传入的参数为URL地址,由spring security自行封装为FilterInvocation对象
	 * 
	 *            return null; //直接通过判断,可以访问该资源 return returnUrl;
	 *            //用于跳转至RoleAccessDecisionManager，进行权限判断
	 */
	public Collection<ConfigAttribute> getAttributes(Object object)
			throws IllegalArgumentException {
		FilterInvocation invocation = (FilterInvocation) object;
		String address = invocation.getRequest().getServletPath();
		
		//开发使用，不用登入，所有URL直接放行。
		if(true){
			return null;
		}
		
		//如URL地址加有 .intercept 标识，则直接放行，用于前端方法访问
		if(address.lastIndexOf(intercept) != -1){
			return null;
		}
		// 判断不拦截的方法(前端访问)
		String open = (String) MCNotInterceptTypeMethodMap.get(address);
		if(StringUtils.isNotBlank(open)){
			return null;
		}
		// 判断不拦截的类（包含该类下的所有方法访问）(前端访问)
		for (String string : MCNotInterceptTypeClassMap.keySet()) {
			if (address.lastIndexOf(string) != -1) {
				return null;
			}
		}
		// 后台访问 菜单、方法/类 判断
		if (getJudgeUser(invocation)) {
			// 判断访问方法或类(该类的方法)的权限  
			if (address.lastIndexOf(HTML) == -1 && address.lastIndexOf(JSP) == -1 && address.lastIndexOf(html) == -1 && address.lastIndexOf(jsp) == -1) {
				//方法 (个人权限)
				Set<Permission> setMethodMy = MCInInterceptTypeMethodMapMy.get(address);
				if(setMethodMy != null){
					this.MethodMySet = setMethodMy;
				}else{
					this.MethodMySet = null;
				}
				//方法(角色权限)
				Set<Permission> setMethodRole = MCInInterceptTypeMethodMapRole.get(address);
				if(setMethodRole != null){
					this.MethodRoleSet = setMethodRole;
				}else{
					this.MethodRoleSet = null;
				}
				//类 (个人权限)
				Set<Permission> setClassMy = null;
				for (Entry<String, Set<Permission>> entry : MCInInterceptTypeClassMapMy.entrySet()) {
					if(address.indexOf(entry.getKey()) != -1){
						setClassMy = entry.getValue();
					}
				}
				if(setClassMy != null){
					this.ClassMySet = setClassMy;
				}else{
					this.ClassMySet = null;
				}
				// 类 (角色权限)
				Set<Permission> setClassRole = null;
				for (Entry<String, Set<Permission>> entry : MCInInterceptTypeClassMapRole.entrySet()) {
					if(address.indexOf(entry.getKey()) != -1){
						setClassRole = entry.getValue();
					}
				}
				if(setClassRole != null){
					this.ClassRoleSet = setClassRole;
				}else{
					this.ClassRoleSet = null;
				}
				// 没有配置类和方法 ，则默认为都可以访问。
				if (this.MethodMySet != null || this.MethodRoleSet != null || this.ClassMySet != null || this.ClassRoleSet != null) {
					return returnUrl;
				} else {
					return null;
				}
			} else {
			    // 判断访问菜单的权限(个人权限)
				Set<Permission> setUser = userMap.get(address);
				if (setUser != null) {
					this.USet = setUser;
				}else{
					this.USet = null;
				}
				// 判断访问菜单的权限(角色权限)
				Set<Permission> setRole = urlMap.get(address);
				if (setRole != null) {
					this.RSet = setRole;
				}else{
					this.RSet = null;
				}
				// 没有配置菜单 则默认为都可以访问。
				if (this.USet != null || this.RSet != null) {
					return returnUrl;
				} else {
					return null;
				}
			}
		} else {
			return null;
		}
	}

	/**
	 * 访问后台页面时进行判断用户是否登入 原因： 1：第一次访问html页面时，用户未登入直接跳转至登入页面。
	 * 2：在用户未登入的情况下，防止浏览器缓存了第一次访问时存在的html页面，从而第二次访问html页面，浏览器不会发出页面请求，
	 * 而直接执行js(ajax)来进行访问。
	 * 
	 * @return true 说明用户登入。 ajax方式访问return
	 *         false,并向该ajax输出InvalidAccessNotLoggedIn,
	 *         并由每个页面的index.js中引入的$.ajaxSetup监听来进行处理。 http方式访问，则直接抛异常。
	 */
	public boolean getJudgeUser(FilterInvocation invocation) {
		try {
			User user = (User) invocation.getRequest().getSession(false)
					.getAttribute("user");
			if (user != null) {
				if (user.getId().equals(AccountUtils.ADMIN)|| // 访问者是系统管理员or有管理员权限or管理员角色
						user.getId().equals(AccountUtils.ROLEADMIN)||
						user.getId().equals(AccountUtils.PERMISSIONADMIN)) {
					return false; // admin用户，则直接放行
				}
				return true;
			} else {
				boolean isAjax = "XMLHttpRequest".equals(invocation
						.getRequest().getHeader("X-Requested-With"));
				if (isAjax) {
					AccountUtils.getWriter(invocation.getResponse(),
							AccountUtils.JSON_TYPE, "InvalidAccessNotLoggedIn");
				} else {
					throw new AccessDeniedException("该用户没有登入!");
				}
			}
		} catch (Exception e) {
			boolean isAjax = "XMLHttpRequest".equals(invocation.getRequest()
					.getHeader("X-Requested-With"));
			if (isAjax) {
				try {
					AccountUtils.getWriter(invocation.getResponse(),
							AccountUtils.JSON_TYPE, "InvalidAccessNotLoggedIn");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} else {
				throw new AccessDeniedException("该用户没有登入!");
			}
		}
		return false;
	}

	/**
	 * 项目启动时执行此方法
	 */
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		try {
			//菜单URL 角色权限和个人权限
			this.getAllConfigAttributesCallRole();
			this.getAllConfigAttributesCallPermission();
			//方法和类URL 前端访问不拦截
			this.getSelectAllNotMethodClassIntercept();
			//方法URL和类URL 	后台  角色权限和个人权限
			this.getSelectAllMethodClassInterceptRole();
			this.getSelectAllMethodClassInterceptUser();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 加载所有不拦截的方法和类
	 */
	public void getSelectAllNotMethodClassIntercept(){
		List<MethodClass> methodNot = methodClassMapper.selectAllMethodClassIntercept();
		for (MethodClass methodClass : methodNot) {
			if(methodClass.getType().equals("0")){
				MCNotInterceptTypeMethodMap.put(methodClass.getUrl(), methodClass.getId());
			}else{
				MCNotInterceptTypeClassMap.put(methodClass.getUrl(), methodClass.getId());
			}
		}
	}
	
	/**
	 * 加载方法或类URL引用的权限来查询可以访问的角色(角色权限) 
	 */
	public void getSelectAllMethodClassInterceptRole(){
		List<MethodClass> method = methodClassMapper.selectAllMethodClassInterceptRole();
		for (MethodClass methodClass : method) {
			if(methodClass.getType().equals("0")){
				MCInInterceptTypeMethodMapRole.put(methodClass.getUrl(), methodClass.getPermissionSet());
			}else{
				MCInInterceptTypeClassMapRole.put(methodClass.getUrl(), methodClass.getPermissionSet());
			}
		}
	}
	
	/**
	 * 加载方法或类URL引用的权限来查询可以访问的用户(个人权限)
	 */
	public void getSelectAllMethodClassInterceptUser(){
		List<MethodClass> method = methodClassMapper.selectAllMethodClassInterceptUser();
		for (MethodClass methodClass : method) {
			if(methodClass.getType().equals("0")){
				MCInInterceptTypeMethodMapMy.put(methodClass.getUrl(), methodClass.getPermissionSet());
			}else{
				MCInInterceptTypeClassMapMy.put(methodClass.getUrl(), methodClass.getPermissionSet());
			}
		}
	}
	
	/**
	 * 加载权限对应角色（菜单URL）
	 */
	public void getAllConfigAttributesCallRole() throws Exception {
		List<Url> urlRoleList = urlMapper.findAllTwoRole();
		for (Url url : urlRoleList) {
			urlMap.put(url.getAddress(), url.getPermissionSet());
		}
	}
	
	/**
	 * 加载权限对应的用户（菜单URL）
	 */
	public void getAllConfigAttributesCallPermission() throws Exception {
		List<Url> twoUser = urlMapper.findAllTwoUser();
		for (Url url : twoUser) {
			userMap.put(url.getAddress(), url.getPermissionSet());
		}
	}

	public boolean supports(Class<?> clazz) {
		return true;
	}

	public List<Map<String, Object>> findAllParentNode() {
		return urlMapper.findAllParentNode();
	}

	public List<Map<String, Object>> selectPkPermissionUrl(List<String> list) {
		return urlMapper.selectPkPermissionUrl(list);
	}
	
	@Transactional
	@LogDescription(name="删除菜单URL")
	public void deleteByPkPermission(String id) throws Exception {
		int selectByPkPermission = urlMapper.selectByPkPermission(id);
		super.deleteByPk(id);
		urlMapper.deletePKPermission(id);
		// 判断该菜单是否被权限引用，如果引用，则删除掉在内在中的权限的引用菜单
		if(selectByPkPermission > 0){
			this.getAllConfigAttributesCallPermission();
			this.getAllConfigAttributesCallRole();
		}
		/*遍历删除该菜单下的子菜单*/
		List<Url> list = getByParentId(id);
		if(list!=null && list.size()>0){
			for(Url u:list)
				deleteByPkPermission(u.getId());
		}
	}
	
	public List<Url> getByParentId(String parentId){
		if(StringUtils.isNotEmpty(parentId)){
			Url u = new Url();
			u.setParentId(parentId);
			try {
				return urlMapper.selectAll(u);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public Set<Permission> getUSet() {
		return USet;
	}

	public Set<Permission> getRSet() {
		return RSet;
	}
	
	public Set<Permission> getMethodMySet() {
		return MethodMySet;
	}

	public Set<Permission> getMethodRoleSet() {
		return MethodRoleSet;
	}

	public Set<Permission> getClassMySet() {
		return ClassMySet;
	}

	public Set<Permission> getClassRoleSet() {
		return ClassRoleSet;
	}

	public Pager<Url> selectPageFather(Url url, Integer pageNum,
			Integer pageSize) throws Exception {
		Page<Url> page = PageHelper.startPage(pageNum == null ? 1 : pageNum,
				pageSize == null ? 10 : pageSize, true);
		List<Map<String, Object>> pageList = urlMapper.selectPageFather(url);
		return new Pager(pageList);
	}

	public Map<String, Object> selectUrlAndParentByPk(String id) {
		return urlMapper.selectUrlAndParentByPk(id);
	}

	public List<Url> findAllNoDispaly() {
		return urlMapper.findAllNoDispaly();
	}

	public JSONArray findAllFrontParentNode(HttpServletRequest request) {
		List<Url> parentNode = urlMapper.findAllFrontParentNode();
		List<Url> sonNode = urlMapper.findAllFrontSonNode();
		JSONArray array_ = new JSONArray();
		Locale locale = RequestContextUtils.getLocaleResolver(request).resolveLocale(request);
		for (Url url : parentNode) {
			JSONArray array = new JSONArray();
			String uid = (String) url.getId();//url.get("u_id");
			for (Url url_ : sonNode) {
				if (uid.equals(url_.getParentId()/*url_.get("parentId")*/)) {
					JSONObject json = new JSONObject();					
					json.put("text", locale!=null && locale.getLanguage().equals("en")?url_.getEname():url_.getUrlname());//url_.get("url_name"));
					//生活和单位的url改为拼接u_id
					//if(url.getCode().equals("SH") || url.getCode().equals("DW")){//url_.get("address"));
					//	json.put("url", url_.getAddress()+"?typeId="+url_.getId());
					//}else {//url_.get("address"));
						json.put("url", url_.getAddress());
					//}
					json.put("img", url_.getClassName());//url_.get("className"));
					json.put("code", url_.getCode());//url_.get("code"));
					json.put("template", url_.getTemplate());//url_.get("template"));
					json.put("id", url_.getId());
					array.add(json);
				}
			}
			if (array.size() > 0) {
				JSONObject json_ = new JSONObject();
				json_.put("text", locale!=null && locale.getLanguage().equals("en")?url.getEname():url.getUrlname());//url.get("url_name"));
				json_.put("img", url.getClassName());//url.get("className"));
				json_.put("code", url.getCode());//url.get("code"));
				json_.put("id", uid);//url.get("u_id"));
				json_.put("children", array);
				array_.add(json_);
			}
		}
		return array_;
	}

	public int findMaxOrder() {
		return urlMapper.findMaxOrder();
	}

	public List<Map<String, Object>> findAllParentNodeAround() {
		return urlMapper.findAllParentNodeAround();
	}

	public boolean selectByPkCode(String id,String code) {
		return urlMapper.selectByPkCode(id,code) > 0 ? false : true;
	}
	
	public SysFile uploadIcon(HttpServletRequest request){
		//创建一个通用的多部分解析器.  
        CommonsMultipartResolver cmr = new CommonsMultipartResolver(request.getSession().getServletContext());  
        //设置编码  
        cmr.setDefaultEncoding("utf-8");  
        if (cmr.isMultipart(request)) {  
        	MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;    
            List<MultipartFile> fileList = multipartRequest.getFiles("files");
            if(fileList!=null && fileList.size()>0){
            	String fileId = fileService.upload(fileList.get(0), Module.SYSTEM.getCode(), request);
            	return fileService.load(fileId);
            }
        }
        return null;
	}
	
	public List<Url> getByParentCode(String parentCode) throws Exception{
		if(StringUtils.isEmpty(parentCode)) throw new Exception("调用错误:参数不能为空!");
		return urlMapper.getByParentCode(parentCode);
	}

}
