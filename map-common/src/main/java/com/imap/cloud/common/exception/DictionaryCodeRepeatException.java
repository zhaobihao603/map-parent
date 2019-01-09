package com.imap.cloud.common.exception;

public class DictionaryCodeRepeatException extends Exception {
	private static final long serialVersionUID = 7900668652249940140L;

	public DictionaryCodeRepeatException() {
	}

	public DictionaryCodeRepeatException(String message, Throwable cause) {
		super(message, cause);
	}

	public DictionaryCodeRepeatException(String message) {
		super(message);
	}

	public DictionaryCodeRepeatException(Throwable cause) {
		super(cause);
	}
}
