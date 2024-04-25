package com.watermelon.exception;

import com.watermelon.utils.MessagesUtils;

public class EmailMessagingException extends RuntimeException{

	private final String message;

	public EmailMessagingException(String errorCode, Object... var2) {
		this.message = MessagesUtils.getMessage(errorCode, var2);
	}

	@Override
	public String getMessage() {
		return message;
	}
}
