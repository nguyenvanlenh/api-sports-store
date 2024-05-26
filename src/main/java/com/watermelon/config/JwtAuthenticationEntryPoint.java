package com.watermelon.config;

import java.io.IOException;
import java.sql.Timestamp;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.watermelon.exception.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {
    	
    	response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		ErrorResponse errorResponse = ErrorResponse.builder()
				.status(HttpServletResponse.SC_UNAUTHORIZED)
				.message("Unauthorized")
				.path(request.getRequestURI())
				.timestamp(new Timestamp(System.currentTimeMillis()).toString())
				.build();
		
		byte[] body = new ObjectMapper().writeValueAsBytes(errorResponse);
		
		response.getOutputStream().write(body);
		response.flushBuffer();
    }
    @Override
    public void afterPropertiesSet() {
    	setRealmName("JWT Authentication");
    	super.afterPropertiesSet();
    }
    
}
