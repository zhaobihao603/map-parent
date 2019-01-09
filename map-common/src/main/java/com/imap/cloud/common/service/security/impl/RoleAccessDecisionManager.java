package com.imap.cloud.common.service.security.impl;

import java.util.Collection;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import com.imap.cloud.common.aop.annotation.LogDescription;
import com.imap.cloud.common.entity.security.Permission;
import com.imap.cloud.common.entity.security.User;

/**
 * 访问决策管理器
 * @author 冯林
 */
@LogDescription(name="URL访问决策管理器")
@Service("accessDecisionManager")
public class RoleAccessDecisionManager implements AccessDecisionManager {
	
	@Resource
	private UrlServiceImpl urlService;
	
	/**
	 * AccessDecisionManager：访问决策管理器，方法decide：决定用户是否有权限访问,通过就没有异常，
	 * 判断当前用户是否有该URL的访问权限
	 */
	public void decide(Authentication authentication, Object object,
			Collection<ConfigAttribute> configAttributes)
			throws AccessDeniedException, InsufficientAuthenticationException {
	  	    User user;
			try{
				user = (User) authentication.getPrincipal();
			}catch(Exception e){
				throw new AccessDeniedException("该用户没有登入!");
			}
			//方法、类(方法)访问权限判断	(个人权限，角色权限)
			//类
			Set<Permission> classMySet = urlService.getClassMySet();
			if(classMySet != null){
				if(judgeUser(classMySet,user)){
					return;
				}
			}
			Set<Permission> classRoleSet = urlService.getClassRoleSet();
			if(classRoleSet != null){
				if(judgeRole(classRoleSet,authentication)){
					return;
				};
			}
			//方法
			Set<Permission> methodMySet = urlService.getMethodMySet();
			if(methodMySet != null){
				if(judgeUser(methodMySet,user)){
					return;
				}
			}
			Set<Permission> methodRoleSet = urlService.getMethodRoleSet();
			if(methodRoleSet != null){
				if(judgeRole(methodRoleSet,authentication)){
					return;
				};
			}
			//菜单访问权限判断	(个人权限，角色权限)
			Set<Permission> set = urlService.getUSet();
			if(set != null){
				if(judgeUser(set,user)){
					return;
				}
			}
			Set<Permission> set_ = urlService.getRSet();
			if(set_ != null){
				if(judgeRole(set_,authentication)){
					return;
				};
			}
			
			throw new AccessDeniedException("没有权限访问此页面!");
	}
	@LogDescription(name="配置属性")
	public boolean supports(ConfigAttribute attribute) {
		return true;
	}

	public boolean supports(Class<?> clazz) {
		return true;
	}
	
	/**
	 * 根据访问的URL对应的权限引用的角色来和当前用户的角色权限进行判断
	 * @param set
	 * @param authentication
	 * @return
	 */
	private boolean judgeRole(Set<Permission> set,Authentication authentication){
		for (Permission permission : set) {
			for(GrantedAuthority userRole:authentication.getAuthorities()){
				for(ConfigAttribute urlRole:permission.getRoleSet()){
					if(userRole.getAuthority().equals(urlRole.getAttribute())) return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 根据访问的URL对应的权限引用的用户来和当前用户的个人权限进行判断
	 * @param set
	 * @param user
	 * @return
	 */
	private boolean judgeUser(Set<Permission> set,User user){
		for (Permission permission : set) {
			for(User user_ : permission.getUserSet()){
				if(user_.getId().equals(user.getId())) return true;
			}
		}
		return false;
	}
}
