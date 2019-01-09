package com.imap.cloud.common.aop;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.imap.cloud.common.entity.system.SysLog;
import com.imap.cloud.common.service.system.LogService;
import com.imap.cloud.common.util.RecordBuilder;

@Component
@Aspect
public class AnnotationAspect {
	private final Log log = LogFactory.getLog(getClass());
	@Autowired
	private LogService logService;

	@Around("@annotation(com.imap.cloud.common.aop.annotation.LogMethod)")
	private Object Log(ProceedingJoinPoint jp) {
		Object r = null;
		try {
			r = jp.proceed();
			SysLog record = RecordBuilder.createOperationalRecord(jp);
			if (record != null) {
				record.setLogTime(new Date());
				this.logService.createRecord(record,this.logService.getHandler());
			} else {
				this.log.error("无法创建操作记录对象");
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return r;
	}
}
