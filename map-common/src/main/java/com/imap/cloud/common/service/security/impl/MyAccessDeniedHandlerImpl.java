package com.imap.cloud.common.service.security.impl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Service;

import com.imap.cloud.common.util.AccountUtils;

/**
 *  用于处理用户登入过后
 *  ajax请求和URL请求，访问该页面没有权限时触发
 * @author 冯林
 *
 */
@Service("accessDeniedHandler")
public class MyAccessDeniedHandlerImpl implements AccessDeniedHandler {
	
	@Override   
	public void handle(HttpServletRequest request,HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException,
			ServletException {
		boolean isAjax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
		if (isAjax) {
			AccountUtils.getWriter(response, AccountUtils.JSON_TYPE, "InvalidAccessNotLoggedIn");
		} else {
			response.sendRedirect("/templet/login.html?error=access-denied");
		}
	}

}
