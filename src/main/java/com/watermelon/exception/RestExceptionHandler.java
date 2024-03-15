package com.watermelon.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.watermelon.model.error.ResponseError;

@ControllerAdvice
public class RestExceptionHandler {

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ResponseError> handleNotFoundException(NotFoundException ex){
		ResponseError error =new ResponseError(HttpStatus.NOT_FOUND.name(),ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}
	@ExceptionHandler(ForbiddenException.class)
	public ResponseEntity<ResponseError> handleForbiddenException(ForbiddenException ex){
		ResponseError error = new ResponseError(HttpStatus.FORBIDDEN.name(),ex.getMessage());
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
	}
}
