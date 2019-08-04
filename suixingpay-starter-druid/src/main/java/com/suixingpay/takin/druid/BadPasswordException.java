package com.suixingpay.takin.druid;

/**
 * @description: BadPasswordException.java
 * @author Tieli.Ma
 */
public class BadPasswordException extends RuntimeException {

	private static final long serialVersionUID = 1549515490517455746L;

	public BadPasswordException() {

	}

	public BadPasswordException(String message) {
		super(message);
	}

	public BadPasswordException(String message, Throwable cause) {
		super(message, cause);
	}

	public BadPasswordException(Throwable cause) {
		super(cause);
	}
}
