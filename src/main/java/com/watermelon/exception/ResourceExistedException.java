package com.watermelon.exception;

import com.watermelon.utils.MessagesUtils;

public class ResourceExistedException extends RuntimeException {
	
	private final String message;

	  public ResourceExistedException(String errorCode, Object... var2) {
	    this.message = MessagesUtils.getMessage(errorCode, var2);
	  }

	  @Override
	  public String getMessage() {
	    return message;
	  }
}
