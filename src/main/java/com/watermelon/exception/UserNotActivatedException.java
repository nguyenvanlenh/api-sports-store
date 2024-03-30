package com.watermelon.exception;

import org.springframework.security.core.AuthenticationException;

public class UserNotActivatedException extends AuthenticationException {

	
	private static final long serialVersionUID = -6409464354660445088L;

	public UserNotActivatedException(String message) {
		super(message);
	}

	public UserNotActivatedException(String message, Throwable t) {
		super(message, t);
	}
}
