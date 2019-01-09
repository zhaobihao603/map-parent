package com.imap.cloud.common.aop.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ java.lang.annotation.ElementType.TYPE,
		java.lang.annotation.ElementType.METHOD,
		java.lang.annotation.ElementType.PARAMETER })
public @interface LogDescription {
	public abstract String name();
}
