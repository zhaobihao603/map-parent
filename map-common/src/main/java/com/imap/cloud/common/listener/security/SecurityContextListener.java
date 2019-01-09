package com.imap.cloud.common.listener.security;

import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.imap.cloud.common.entity.security.User;
import com.imap.cloud.common.service.security.UserService;
import com.imap.cloud.common.util.AccountUtils;


/**
 * 此监听器用来监听session中属性的变化,当session中任意一个属性出现了"新增,删除,替换" 则会执行相应的方法
 * 拿当前登入的用户信息，从session.getAttribute("user")。即可
 * @author 冯林
 *
 */
public class SecurityContextListener implements HttpSessionAttributeListener {
	
	/**
	 * session添加时保存Session
	 */
	@Override
	public void attributeAdded(HttpSessionBindingEvent event) {
		// 判断操作的属性名称是否为: SPRING_SECURITY_CONTEXT
		if(event.getName().equals("SPRING_SECURITY_CONTEXT")){
			SecurityContext context = SecurityContextHolder.getContext();
			Authentication authentication = context.getAuthentication();
			User user = (User)authentication.getPrincipal();
			event.getSession().setAttribute("user", user);
			try {
				UserService userService = (UserService) AccountUtils.getBean("userService", event.getSession().getServletContext());
				//20170210 梁华新 ::此段注释掉，不再使用写入本地json的方式去获取用户的菜单
				//String path = event.getSession().getServletContext().getRealPath("/templet/js/common/json") + "\\"+user.getLogin()+".json";
				//userService.getInit(user,path);
				event.getSession().setAttribute("userMenu", userService.loadUserMenu(user));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void attributeRemoved(HttpSessionBindingEvent event) {
		//event.getSession().removeAttribute("user");  session在退出的时候已经销毁
	}

	/**
	 * session属性有变动时更新User
	 */
	@Override
	public void attributeReplaced(HttpSessionBindingEvent event) {
		if(event.getName().equals("SPRING_SECURITY_CONTEXT")){
			SecurityContext context = SecurityContextHolder.getContext();
			Authentication authentication = context.getAuthentication();
			User user = (User)authentication.getPrincipal();
			event.getSession().setAttribute("user", user);
			try {
				UserService userService = (UserService) AccountUtils.getBean("userService", event.getSession().getServletContext());
				//20170210 梁华新 ::此段注释掉，不再使用写入本地json的方式去获取用户的菜单
				//String path = event.getSession().getServletContext().getRealPath("/templet/js/common/json") + "\\"+user.getLogin()+".json";
				//userService.getInit(user,path);
				event.getSession().setAttribute("userMenu", userService.loadUserMenu(user));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
