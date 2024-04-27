package com.watermelon.exception;

import java.util.stream.Collectors;

import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
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
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class RestExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleNotFoundException(ResourceNotFoundException e,WebRequest request) {
		ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(),
				e.getMessage(),
				getServletPath(request));
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}

	@ExceptionHandler(ForbiddenException.class)
	public ResponseEntity<ErrorResponse> handleForbiddenException(ForbiddenException e,WebRequest request) {
		ErrorResponse error = new ErrorResponse(HttpStatus.FORBIDDEN.value(),
				e.getMessage(),
				getServletPath(request));
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
	}

	@ExceptionHandler(UsernameNotFoundException.class)
	ResponseEntity<ErrorResponse> handlingUsernameNotFoundException(UsernameNotFoundException e,WebRequest request) {
		ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(),
				e.getMessage(),
				getServletPath(request));
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}

	@ExceptionHandler(UserNotActivatedException.class)
	ResponseEntity<ErrorResponse> handlingUserNotActivatedException(UserNotActivatedException e,WebRequest request) {
		ErrorResponse error = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(),
				e.getMessage(),
				getServletPath(request));
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
	}

//	@ExceptionHandler(AuthenticationException.class)
//	ResponseEntity<ErrorResponse> handlingAuthenticationException(AuthenticationException e,WebRequest request) {
//		ErrorResponse error = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(),
//				"Authentication failure",
//				getServletPath(request));
//		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
//	}
	@ExceptionHandler(BadCredentialsException.class)
	ResponseEntity<ErrorResponse> handlingBadCredentialsException(BadCredentialsException e,WebRequest request) {
		ErrorResponse error = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(),
				"The username or password is incorrect",
				getServletPath(request));
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
	}
	
	@ExceptionHandler(ResourceExistedException.class)
	ResponseEntity<ErrorResponse> handlingResourceExistedException(ResourceExistedException e,WebRequest request) {
		ErrorResponse error = new ErrorResponse(HttpStatus.CONFLICT.value(),
				e.getLocalizedMessage(),
				getServletPath(request));
		return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	ResponseEntity<ErrorResponse> handlingMethodArgumentNotValidException(MethodArgumentNotValidException e,
			WebRequest request) {

		String errors = e.getBindingResult().getFieldErrors()
				.stream().map(
				FieldError::getDefaultMessage)
				.collect(Collectors.joining(", "));

		ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), errors,
				getServletPath(request));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);

	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException e,WebRequest request) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
						"Param invalid",
						getServletPath(request)));
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
	public ResponseEntity<ErrorResponse> handleException(Exception e,HttpServletRequest request) {
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
		return ResponseEntity.status(httpStatus).body(new ErrorResponse(httpStatus.value(),
				e.getMessage(),
				request.getRequestURI()));
	}
	@ExceptionHandler({ SignatureException.class, ExpiredJwtException.class, AccessDeniedException.class,MalformedJwtException.class })
	public ResponseEntity<ErrorResponse> handleSecurityException(Exception e, WebRequest request) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN)
				.body(new ErrorResponse(HttpStatus.FORBIDDEN.value(),
						e.getMessage(),
						getServletPath(request)));
	}
	
	private String getServletPath(WebRequest webRequest) {
	    ServletWebRequest servletRequest = (ServletWebRequest) webRequest;
	    return servletRequest.getRequest().getServletPath();
	  }
}
