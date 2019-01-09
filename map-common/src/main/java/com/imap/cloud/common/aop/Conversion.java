package com.imap.cloud.common.aop;

import com.imap.cloud.common.exception.ConversionException;

public abstract interface Conversion {
	public abstract String toIdentifier(Object paramObject)
			throws ConversionException;
}
