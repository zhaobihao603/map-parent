package com.imap.cloud.common.exception;

public class FieldNotFoundException extends Exception {
	private static final long serialVersionUID = 7900668652249940140L;

	public FieldNotFoundException() {
	}

	public FieldNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public FieldNotFoundException(String message) {
		super(message);
	}

	public FieldNotFoundException(Throwable cause) {
		super(cause);
	}
}
