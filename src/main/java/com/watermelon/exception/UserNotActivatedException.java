package com.watermelon.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;
@ResponseStatus(value = HttpStatus.LOCKED)
public class UserNotActivatedException extends AuthenticationException {

	
	private static final long serialVersionUID = -6409464354660445088L;

	public UserNotActivatedException(String message) {
		super(message);
	}

	public UserNotActivatedException(String message, Throwable t) {
		super(message, t);
	}
}
