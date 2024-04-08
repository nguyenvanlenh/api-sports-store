package com.watermelon.filter;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.watermelon.exception.ErrorResponse;
import com.watermelon.exception.ForbiddenException;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExceptionHandlerFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			filterChain.doFilter(request, response);
		} catch (Exception e) {
			HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
			if(e instanceof ForbiddenException) {
				httpStatus = HttpStatus.FORBIDDEN;
			}
			if(e instanceof ExpiredJwtException) {
				httpStatus = HttpStatus.UNAUTHORIZED;
			}
			setErrorResponse(httpStatus,response,e);
		}

	}

	private void setErrorResponse(HttpStatus httpStatus, HttpServletResponse response, Exception e) {
		 response.setStatus(httpStatus.value());
	     response.setContentType(MediaType.APPLICATION_JSON_VALUE);
	     ErrorResponse errorResponse = new ErrorResponse(httpStatus.value(), e.getMessage(), e.getClass().getSimpleName());
	     try {
	    	 String json = convertObjectToJson(errorResponse);
	            response.getWriter().write(json);
		} catch (IOException ex) {
			log.info(ex.getMessage());
		}
		
	}
	 public String convertObjectToJson(Object object) throws JsonProcessingException {
	        if (object == null) {
	            return null;
	        }
	        ObjectMapper mapper = new ObjectMapper();
	        return mapper.writeValueAsString(object);
	    }

}
