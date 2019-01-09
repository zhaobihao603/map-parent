package com.imap.cloud.common.exception;

public class ConversionException extends Exception {
	private static final long serialVersionUID = 7872888115355137540L;

	public ConversionException() {
	}

	public ConversionException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConversionException(String message) {
		super(message);
	}

	public ConversionException(Throwable cause) {
		super(cause);
	}
}
