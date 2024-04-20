package com.watermelon.exception;

import com.watermelon.utils.MessagesUtils;

public class ResourceNotFoundException extends RuntimeException {

	private final String message;

	public ResourceNotFoundException(String errorCode, Object... var2) {
		this.message = MessagesUtils.getMessage(errorCode, var2);
	}

	@Override
	public String getMessage() {
		return message;
	}

}
