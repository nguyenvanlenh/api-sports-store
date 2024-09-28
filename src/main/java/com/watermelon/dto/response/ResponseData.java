package com.watermelon.dto.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseData<T> implements Serializable{
	private static final long serialVersionUID = 1L;
	private int status;
	private String message;
	private T data;

	// PUT, PATCH, DELETE
	public ResponseData(int status, String message) {
		super();
		this.status = status;
		this.message = message;
	}


}
