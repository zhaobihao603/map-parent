package com.imap.cloud.common.service.security.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.imap.cloud.common.util.DesEncryptUtil;

/**
 * 该类主要用于js密码解密 
 * 密码加密使用：CryptoJS v3.1.2 生成的密钥来进行加密解密
 * @author 冯林
 *		UsernamePasswordAuthenticationFilter 该过渡器用于封装表单数据进行封装
 * 		继承重写 attemptAuthentication 方法，实现自定义对表单进行封装
 */
public class MyUsernamePasswordAuthenticationFilter extends
		UsernamePasswordAuthenticationFilter {
	
	private boolean postOnly = true;
	
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
		        throws AuthenticationException{
		if (postOnly && !request.getMethod().equals("POST"))
			throw new AuthenticationServiceException((new StringBuilder())
					.append("Authentication method not supported: ")
					.append(request.getMethod()).toString());
		String username = obtainUsername(request);
		// 对密码进行自行解密
		String password = null;
		try {
			String validateCodeText = request.getParameter("validateCode");
			String validationCodeSeeion = (String) request.getSession(false).getAttribute("validationCode");
			if (StringUtils.isNotBlank(validationCodeSeeion)) {
				if (!validationCodeSeeion.equalsIgnoreCase(validateCodeText)) {
					response.sendRedirect("/templet/login.html?error=validateCodeText-fail"); // 验证码输入不正确
				}
			} else {
				response.sendRedirect("/templet/login.html?error=validateCodeText-overdue"); // 验证码过期
			}
			String pass = obtainPassword(request);
			int index = pass.lastIndexOf("SJmS7OZ!f");
			String password_ = pass.substring(0, index);
			String key = pass.substring(index, pass.length());
			password = DesEncryptUtil.decryption(password_, key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (username == null)
			username = "";
		if (password == null)
			password = "";
		username = username.trim();
		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
				username, password);
		setDetails(request, authRequest);
		return getAuthenticationManager().authenticate(authRequest);
	}
}
