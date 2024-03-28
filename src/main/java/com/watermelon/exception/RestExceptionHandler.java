package com.watermelon.exception;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.watermelon.model.dto.response.ErrorResponse;

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
	
	@ExceptionHandler(UsernameNotFoundException.class)
	ResponseEntity<ErrorResponse> handlingUsernameNotFoundException(UsernameNotFoundException e){
		ErrorResponse error =new ErrorResponse(HttpStatus.NOT_FOUND.name(),e.getLocalizedMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(error);
	}
	@ExceptionHandler(AuthenticationException.class)
	ResponseEntity<ErrorResponse> handlingAuthenticationException(AuthenticationException e){
		ErrorResponse error =new ErrorResponse(HttpStatus.UNAUTHORIZED.name(),e.getLocalizedMessage());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body(error);
	}
	@ExceptionHandler(ResourceExistedException.class)
	ResponseEntity<ErrorResponse> handlingResourceExistedException(ResourceExistedException e){
		ErrorResponse error =new ErrorResponse(HttpStatus.CONFLICT.name(),e.getLocalizedMessage());
		return ResponseEntity.status(HttpStatus.CONFLICT)
				.body(error);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	ResponseEntity<?> handlingMethodArgumentNotValidException(MethodArgumentNotValidException e){
		 Map<String, Object> responseBody = new LinkedHashMap<>();
	        responseBody.put("timestamp", new Date());
	         
	        List<String> errors = e.getBindingResult().getFieldErrors()
	            .stream()
	            .map(x -> x.getDefaultMessage())
	            .collect(Collectors.toList());
	         
	        responseBody.put("errors", errors);
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
		
	}
}
