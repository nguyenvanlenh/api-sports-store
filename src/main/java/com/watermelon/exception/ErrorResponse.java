package com.watermelon.exception;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
public class ErrorResponse {
	int status;
	String message;
	String path;
	String timestamp;

	public ErrorResponse(int status, String message, String path) {
		super();
		this.status = status;
		this.message = message;
		this.path = path;
		this.timestamp = new Timestamp(System.currentTimeMillis()).toString();
	}

}
