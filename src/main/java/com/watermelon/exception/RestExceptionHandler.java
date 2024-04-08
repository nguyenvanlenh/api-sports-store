package com.watermelon.exception;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class RestExceptionHandler {

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex) {
		ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}

	@ExceptionHandler(ForbiddenException.class)
	public ResponseEntity<ErrorResponse> handleForbiddenException(ForbiddenException ex) {
		ErrorResponse error = new ErrorResponse(HttpStatus.FORBIDDEN.value(), ex.getMessage());
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
	}

	@ExceptionHandler(UsernameNotFoundException.class)
	ResponseEntity<ErrorResponse> handlingUsernameNotFoundException(UsernameNotFoundException e) {
		ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getLocalizedMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}

	@ExceptionHandler(AuthenticationException.class)
	ResponseEntity<ErrorResponse> handlingAuthenticationException(AuthenticationException e) {
		ErrorResponse error = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), e.getLocalizedMessage());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
	}

	@ExceptionHandler(ResourceExistedException.class)
	ResponseEntity<ErrorResponse> handlingResourceExistedException(ResourceExistedException e) {
		ErrorResponse error = new ErrorResponse(HttpStatus.CONFLICT.value(), e.getLocalizedMessage());
		return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	ResponseEntity<?> handlingMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		Map<String, Object> responseBody = new LinkedHashMap<>();
		responseBody.put("status", HttpStatus.BAD_REQUEST.value());

		Map<String, String> errors = e.getBindingResult().getFieldErrors().stream().collect(
				Collectors.toMap(fieldError -> fieldError.getField(), fieldError -> fieldError.getDefaultMessage()));

		responseBody.put("errors", errors);
		responseBody.put("timestamp", new Date());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);

	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<?> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getLocalizedMessage()));
	}

	@ExceptionHandler({ 
		HttpRequestMethodNotSupportedException.class,
		HttpMediaTypeNotSupportedException.class,
		HttpMediaTypeNotAcceptableException.class,
		MissingPathVariableException.class,
		ConversionNotSupportedException.class,
		HttpMessageNotWritableException.class,
		MissingServletRequestParameterException.class,
		ServletRequestBindingException.class,
		TypeMismatchException.class,
		HttpMessageNotReadableException.class,
		MissingServletRequestPartException.class,
		BindException.class, NoHandlerFoundException.class,
		AsyncRequestTimeoutException.class })
	public ResponseEntity<ErrorResponse> handleException(Exception e) {
		HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
		if (e instanceof HttpRequestMethodNotSupportedException) {
			httpStatus = HttpStatus.METHOD_NOT_ALLOWED;
		} else if (e instanceof HttpMediaTypeNotSupportedException) {
			httpStatus = HttpStatus.UNSUPPORTED_MEDIA_TYPE;
		} else if (e instanceof HttpMediaTypeNotAcceptableException) {
			httpStatus = HttpStatus.NOT_ACCEPTABLE;
		} else if (e instanceof MissingPathVariableException 
				|| e instanceof ConversionNotSupportedException
				|| e instanceof HttpMessageNotWritableException) {
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		} else if (e instanceof NoHandlerFoundException 
				|| e instanceof AsyncRequestTimeoutException) {
			httpStatus = HttpStatus.NOT_FOUND;
		}
		return ResponseEntity.status(httpStatus).body(new ErrorResponse(httpStatus.value(), e.getMessage()));
	}
}
