package com.watermelon.dto.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseData<T> implements Serializable{

	private final int status;
	private final String message;
	private T data;

	// PUT, PATCH, DELETE
	public ResponseData(int status, String message) {
		super();
		this.status = status;
		this.message = message;
	}

	
	// GET, POST
	public ResponseData(int status, String message, T data) {
		super();
		this.status = status;
		this.message = message;
		this.data = data;
	}

}
