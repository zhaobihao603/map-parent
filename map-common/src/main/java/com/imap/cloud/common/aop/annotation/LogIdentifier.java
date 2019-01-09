package com.imap.cloud.common.aop.annotation;

import org.apache.commons.lang3.Conversion;

import com.imap.cloud.common.enums.IdentifierType;

public @interface LogIdentifier {
	public abstract IdentifierType type();

	public abstract String name();

	public abstract Class<? extends Conversion> conversion();
}

