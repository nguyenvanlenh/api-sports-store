package com.watermelon.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.watermelon.viewandmodel.error.ResponseError;

@ControllerAdvice
public class RestExceptionHandler {

	@ExceptionHandler(NotFoundException.class)
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	public ResponseError handleNotFoundException(NotFoundException ex){
		return new ResponseError(HttpStatus.NOT_FOUND.name(),ex.getMessage());
	}
	@ExceptionHandler(ForbiddenException.class)
	@ResponseStatus(code = HttpStatus.FORBIDDEN)
	public ResponseError handleForbiddenException(ForbiddenException ex){
		return new ResponseError(HttpStatus.FORBIDDEN.name(),ex.getMessage());
	}
}
