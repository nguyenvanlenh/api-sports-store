package com.watermelon.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@AllArgsConstructor
public class ErrorResponse{
	int status;
	String message;
	String classNameException;
	public ErrorResponse(int status, String message) {
		super();
		this.status = status;
		this.message = message;
	}
	
}
