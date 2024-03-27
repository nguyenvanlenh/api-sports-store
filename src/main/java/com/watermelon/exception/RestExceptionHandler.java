package com.watermelon.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.watermelon.model.response.ErrorResponse;

@ControllerAdvice
public class RestExceptionHandler {

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex){
		ErrorResponse error =new ErrorResponse(HttpStatus.NOT_FOUND.name(),ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}
	@ExceptionHandler(ForbiddenException.class)
	public ResponseEntity<ErrorResponse> handleForbiddenException(ForbiddenException ex){
		ErrorResponse error = new ErrorResponse(HttpStatus.FORBIDDEN.name(),ex.getMessage());
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
	}
}
