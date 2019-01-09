package com.imap.cloud.common.aop;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.Vector;

import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.imap.cloud.common.aop.annotation.LogDescription;
import com.imap.cloud.common.entity.security.User;
import com.imap.cloud.common.entity.system.SysLog;
import com.imap.cloud.common.service.system.LogService;
import com.imap.cloud.common.util.ReflectionUtils;

/**
 * 服务层的操作日志的写入
 * 
 * 
 *
 */
public class SysLogAspect {

	@Autowired
	private LogService logService;

	private void writeLog(JoinPoint joinPoint) throws Exception {

		if (joinPoint.getTarget() instanceof LogService) {
			return;
		}
		
		// 被AOP切面的类
		Class<?> cls = joinPoint.getTarget().getClass();
		//获取类上的注解值
	    LogDescription description = (LogDescription) cls.getAnnotation(LogDescription.class);
	    if(cls.isAnnotationPresent(LogDescription.class) || description == null) return;

		// 创建日志记录实体
		SysLog syslog = new SysLog();
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication authentication = context.getAuthentication();
		if (authentication != null) {
			if(authentication.getPrincipal().equals("anonymousUser")){
				syslog.setUsername("anonymousUser");
			}else{
				User user = (User) authentication.getPrincipal();
				syslog.setUsername(user.getName());
			}
		} else {
			syslog.setUsername("未登录");
		}

		syslog.setLogTime(new Date());
		
		// 被AOP切面类中的方法
		String methodName = joinPoint.getSignature().getName();

		syslog.setEventObject(cls.getName());
		syslog.setEventMethod(methodName);
		
		if (description != null) {
			syslog.setEventObjectDesc(description.name());
		}
		
		//获取方法上的注解
		Method meth = null;
		try {
			Vector argClasses = new Vector();
			for (Object obj : joinPoint.getArgs()) {
				argClasses.add(obj.getClass());
			}
			meth = ReflectionUtils.findMethod(cls, methodName,
					(Class[]) argClasses.toArray(new Class[argClasses.size()]));
		} catch (Exception e) {
			meth = org.springframework.util.ReflectionUtils.findMethod(cls, methodName);
		}
		if (meth != null)
			description = (LogDescription) meth.getAnnotation(LogDescription.class);
		if (description != null) {
			syslog.setEventMethodDesc(description.name());
		}

		// 设置传入方法的参数对象
		Object[] args = joinPoint.getArgs();
		// 把参数对象转换字符串
		syslog.setParams(Arrays.toString(args));
		// 保存日志信息
		logService.save(syslog);

	}
}
