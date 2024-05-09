package com.watermelon.exception;

import java.util.stream.Collectors;

import javax.naming.AuthenticationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class RestExceptionHandler {

	@ExceptionHandler({
			ResourceNotFoundException.class,
			NoHandlerFoundException.class
	})
	public ResponseEntity<ErrorResponse> handleNotFoundException(Exception e,WebRequest request) {
		String message = e.getMessage();
		if(e instanceof NoHandlerFoundException){
			message = "Not found";
		}
		ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(),
				message,
				getServletPath(request));
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}
	@ExceptionHandler({
			ForbiddenException.class,
			AccessDeniedException.class
	})
	public ResponseEntity<ErrorResponse> handleForbiddenException(Exception e,WebRequest request) {
		String message = e.getMessage();
		if(e instanceof AccessDeniedException){
			message = "Access denied";
		}
		ErrorResponse error = new ErrorResponse(HttpStatus.FORBIDDEN.value(),
				message,
				getServletPath(request));
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
	}

	@ExceptionHandler({
		AuthenticationException.class,
		UserNotActivatedException.class,
		BadCredentialsException.class,
		UsernameNotFoundException.class
		})
	ResponseEntity<ErrorResponse> handlingAuthenticationException(Exception e,WebRequest request) {
		String message = e.getMessage();
		
		if(e instanceof UserNotActivatedException ex) {
			message = ex.getMessage();
		}
		if(e instanceof UsernameNotFoundException) {
			message = "Username not found";
		}
		if(e instanceof BadCredentialsException) {
			message = "The username or password is incorrect";
		}
		if (e instanceof AuthenticationException) {
			message = "Authentication failure";
		}
		
		ErrorResponse error = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(),
				message,
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
	@ExceptionHandler(EmailMessagingException.class)
	ResponseEntity<ErrorResponse> handlingEmailMessagingExceptionException(EmailMessagingException e,WebRequest request) {
		ErrorResponse error = new ErrorResponse(HttpStatus.NOT_ACCEPTABLE.value(),
				e.getLocalizedMessage(),
				getServletPath(request));
		return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(error);
	}

	@ExceptionHandler({
		MethodArgumentNotValidException.class,
		ConstraintViolationException.class,
		MissingServletRequestParameterException.class,
		MethodArgumentTypeMismatchException.class,
		MissingPathVariableException.class,
		})
	ResponseEntity<ErrorResponse> handlingBadRequestException(Exception e,
			WebRequest request) {
		String errors = e.getMessage();
		if(e instanceof MethodArgumentNotValidException ex) {
			errors = ex.getBindingResult().getFieldErrors()
					.stream().map(
							FieldError::getDefaultMessage)
					.collect(Collectors.joining(", "));
		}
		if(e instanceof ConstraintViolationException) {
			//validaiton on controller
			errors = errors.substring(errors.indexOf(":")+1).trim();
		}
		if(e instanceof MissingServletRequestParameterException ex) {
			//requestparam is null
			errors = ex.getMessage();
		}
		if(e instanceof MissingPathVariableException ex) {
			// pathvariable is null
			errors = ex.getMessage();
		}
		if(e instanceof MethodArgumentTypeMismatchException) {
			errors = "PathVariable invalid";
		}

		ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), errors,
				getServletPath(request));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);

	}

	@ExceptionHandler({Exception.class, RuntimeException.class})
	public ResponseEntity<ErrorResponse> handleInternalServerException(Exception e,WebRequest request) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
						e.getMessage(),
						getServletPath(request)));
	}

	@ExceptionHandler({ SignatureException.class, ExpiredJwtException.class,MalformedJwtException.class })
	public ResponseEntity<ErrorResponse> handleJWTException(Exception e, WebRequest request) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(),
						e.getMessage(),
						getServletPath(request)));
	}
	
	private String getServletPath(WebRequest webRequest) {
	    ServletWebRequest servletRequest = (ServletWebRequest) webRequest;
	    return servletRequest.getRequest().getServletPath();
	  }
}
