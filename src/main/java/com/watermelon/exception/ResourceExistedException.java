package com.watermelon.exception;

import com.watermelon.utils.MessagesUtils;

public class ResourceExistedException extends RuntimeException {
	
	private String message;

	  public ResourceExistedException(String errorCode, Object... var2) {
	    this.message = MessagesUtils.getMessage(errorCode, var2);
	  }

	  @Override
	  public String getMessage() {
	    return message;
	  }

	  public void setMessage(String message) {
	    this.message = message;
	  }
}
