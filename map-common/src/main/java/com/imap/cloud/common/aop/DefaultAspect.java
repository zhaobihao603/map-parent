package com.imap.cloud.common.aop;

import java.util.Date;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.imap.cloud.common.entity.system.SysLog;
import com.imap.cloud.common.service.system.LogService;
import com.imap.cloud.common.util.RecordBuilder;

@Component
@Aspect
public class DefaultAspect {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private LogService logService;

	//配置切入点,该方法无方法体,主要为方便同类中其他方法使用此处配置的切入点
		@Pointcut("execution(* com.imap.cloud.common.service..*.*(..))")
		public void aspect(){	
			
		}
		
		@Around("aspect()")
		private Object Log(ProceedingJoinPoint jp) {
			Object r = null;
			try {
				r = jp.proceed();
				SysLog record = RecordBuilder.createOperationalRecord(jp);
				if (record != null) {
					record.setLogTime(new Date());
					this.logService.createRecord(record,this.logService.getHandler());
				} else {
					this.logger.error("无法创建操作记录对象");
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}
			return r;
		}
		
		@AfterReturning(pointcut = "execution(* com.imap.cloud.common.service.map.impl.MapLayerServiceImpl.selectPage(..))")
	    public void waiverEmailNotification(JoinPoint jp) {
	        System.out.println("Eamil Notification--------------------------/////-----");
	    }
		
		/**
		 * 当使用 Around以后  AfterReturning的返回值就会为空
		 * @param pjp
		 * @param arg1
		 * @throws Throwable 
		 */
		@Around(value = "execution(* com.imap.cloud.common.service.map.impl.*.*(..))")
		public void aroundGetUser(ProceedingJoinPoint pjp) throws Throwable {
			System.out.println("@Around 执行之前!");
			System.out.println("@Around 执行时参数:");
			pjp.proceed();
			System.out.println("@Around 执行之后!");
		}
		
		/*
		 * 配置前置通知,使用在方法aspect()上注册的切入点
		 * 同时接受JoinPoint切入点对象,可以没有该参数
		 */
		@Before("aspect()")
		public void before(JoinPoint joinPoint){
			if(logger.isInfoEnabled()){
				logger.info("before " + joinPoint);
			}
		}
		
		//配置后置通知,使用在方法aspect()上注册的切入点
		@After("aspect()")
		public void after(JoinPoint joinPoint){
			if(logger.isInfoEnabled()){
				logger.info("after " + joinPoint);
			}
		}
		
		//配置环绕通知,使用在方法aspect()上注册的切入点
		@Around("aspect()")
		public void around(JoinPoint joinPoint){
			long start = System.currentTimeMillis();
			try {
				((ProceedingJoinPoint) joinPoint).proceed();
				long end = System.currentTimeMillis();
				if(logger.isInfoEnabled()){
					logger.info("around " + joinPoint + "\tUse time : " + (end - start) + " ms!");
				}
			} catch (Throwable e) {
				long end = System.currentTimeMillis();
				if(logger.isInfoEnabled()){
					logger.info("around " + joinPoint + "\tUse time : " + (end - start) + " ms with exception : " + e.getMessage());
				}
			}
		}
		
		//配置后置返回通知,使用在方法aspect()上注册的切入点
		@AfterReturning("aspect()")
		public void afterReturn(JoinPoint joinPoint){
			if(logger.isInfoEnabled()){
				logger.info("afterReturn " + joinPoint);
			}
		}
		
		//配置抛出异常后通知,使用在方法aspect()上注册的切入点
		@AfterThrowing(pointcut="aspect()", throwing="ex")
		public void afterThrow(JoinPoint joinPoint, Exception ex){
			if(logger.isInfoEnabled()){
				logger.info("afterThrow " + joinPoint + "\t" + ex.getMessage());
			}
		}
}
