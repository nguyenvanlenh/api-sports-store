package com.watermelon.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.watermelon.utils.MessagesUtils;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class RecaptchaTokenInvalidException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private final String message;

	public RecaptchaTokenInvalidException(String errorCode, Object... var2) {
		this.message = MessagesUtils.getMessage(errorCode, var2);
	}

	@Override
	public String getMessage() {
		return message;
	}

}
