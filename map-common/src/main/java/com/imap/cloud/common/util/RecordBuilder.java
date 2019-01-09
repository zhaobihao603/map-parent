package com.imap.cloud.common.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;

import com.alibaba.druid.util.StringUtils;
import com.imap.cloud.common.aop.Conversion;
import com.imap.cloud.common.aop.annotation.LogArgument;
import com.imap.cloud.common.aop.annotation.LogDescription;
import com.imap.cloud.common.aop.annotation.LogIdentifier;
import com.imap.cloud.common.entity.system.SysLog;
import com.imap.cloud.common.entity.system.SysLogArgs;
import com.imap.cloud.common.exception.ConversionException;
import com.imap.cloud.common.exception.FieldNotFoundException;

public class RecordBuilder {
	private static final Log log = LogFactory.getLog(RecordBuilder.class);

	private static HashMap<Class<? extends Conversion>, Conversion> map = new HashMap();

	public static SysLog createOperationalRecord(
			ProceedingJoinPoint jp) {
		try {
			SysLog record = new SysLog();

			Class cls = jp.getTarget().getClass();
			record.setEventObject(cls.getName());
			LogDescription description = (LogDescription) cls.getAnnotation(LogDescription.class);
			if (description != null) {
				record.setEventObjectDesc(description.name());
			}

			String methName = jp.getSignature().getName();
			record.setEventMethod(methName);
			
			Method meth = null;
			try{
				Vector argClasses = new Vector();
				for (Object obj : jp.getArgs()) {
					argClasses.add(obj.getClass());
				}
				meth = ReflectionUtils.findMethod(cls, methName,
					(Class[]) argClasses.toArray(new Class[argClasses.size()]));
			}catch(Exception e){
				meth = org.springframework.util.ReflectionUtils.findMethod(cls, methName);
			}
			if(meth!=null)
				description = (LogDescription) meth.getAnnotation(LogDescription.class);
			if (description != null) {
				record.setEventMethodDesc(description.name());
			}
			try{
				LogArgument[] annos = getLogArgumentArray(meth);
				Object[] args = jp.getArgs();
				for (int i = 0; i < args.length; i++) {
					record.addArgument(createSysLogArgs(args[i], annos[i]));
				}
			}catch(Exception e){
				
			}
			return record;
		} catch (SecurityException e) {
			log.error("不可能出现的错误", e);
		}
		return null;
	}

	private static LogArgument[] getLogArgumentArray(Method meth) {
		Annotation[][] annos = meth.getParameterAnnotations();
		LogArgument[] result = new LogArgument[annos.length];
		for (int i = 0; i < annos.length; i++) {
			for (Annotation anno : annos[i]) {
				if ((anno instanceof LogArgument)) {
					result[i] = ((LogArgument) anno);
					break;
				}
			}
		}
		return result;
	}

	private static SysLogArgs createSysLogArgs(Object object,
			LogArgument annotation) {
		if (object == null) {
			return null;
		}
		SysLogArgs result = new SysLogArgs();
		result.setClassname(object.getClass().getName());
		if (annotation != null) {
			if (!StringUtils.isEmpty(annotation.name())) {
				result.setDescription(annotation.name());
			}
			result.setIdentifier(getArgumentIdentifier(object,
					annotation.identifier()));
		} else {
			result.setIdentifier(object.toString());
		}

		return result;
	}

	private static String getArgumentIdentifier(Object object,
			LogIdentifier annotation) {
		Object value = null;
		System.out.println(annotation.type().ordinal());
		switch (annotation.type().ordinal() + 1) {
		case 2:
			try {
				Field field = ReflectionUtils.findField(object.getClass(),
						annotation.name());
				if (field == null)
					throw new FieldNotFoundException(object.toString() + "缺少"+ annotation.name() + "字段");
				field.setAccessible(true);
				value = field.get(object);
			} catch (SecurityException e) {
				log.error("设置" + annotation.name()
						+ "字段的 accessible 标志为 true 时请求被拒绝。", e);
			} catch (FieldNotFoundException e) {
				log.error("无法获取注解的 Field 对象", e);
			} catch (IllegalArgumentException e) {
				log.error("无法获取注解 Field 的值", e);
			} catch (IllegalAccessException e) {
				log.error("无法获取注解 Field 的值", e);
			}
			break;
		case 1:
			try {
				Method method = object.getClass().getMethod(annotation.name(),
						new Class[0]);
				value = method.invoke(object, new Object[0]);
			} catch (SecurityException e) {
				log.error("无法获取注解的 Method 对象", e);
			} catch (NoSuchMethodException e) {
				log.error("无法获取注解的 Method 对象", e);
			} catch (IllegalArgumentException e) {
				log.error("无法获取注解 Method 对象的返回值", e);
			} catch (IllegalAccessException e) {
				log.error("无法获取注解 Method 对象的返回值", e);
			} catch (InvocationTargetException e) {
				log.error("无法获取注解 Method 对象的返回值", e);
			}
			break;
		default:
			value = object;
		}
		try {
			if (value != null) {
				Class clz = annotation.conversion();
				if (map.containsKey(clz)) {
					return ((Conversion) map.get(clz)).toIdentifier(value);
				}
				Conversion conver = (Conversion) clz.newInstance();
				map.put(clz, conver);
				return conver.toIdentifier(value);
			}
		} catch (InstantiationException e) {
			log.error("无法创建标识符转换器", e);
		} catch (IllegalAccessException e) {
			log.error("无法创建标识符转换器", e);
		} catch (ConversionException e) {
			log.error("标识符转换器无法转换对象为标识符", e);
		} 

		return null;
	}
}

