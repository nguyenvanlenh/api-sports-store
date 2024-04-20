package com.watermelon.filter;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.watermelon.exception.ErrorResponse;
import com.watermelon.exception.ForbiddenException;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			filterChain.doFilter(request, response);
		} catch (Exception e) {
			HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
			if (e instanceof ForbiddenException) {
				httpStatus = HttpStatus.FORBIDDEN;
			}
			if (e instanceof ExpiredJwtException) {
				httpStatus = HttpStatus.UNAUTHORIZED;
			}
			sendErrorResponse(request, httpStatus, response, e);
		}

	}

	private void sendErrorResponse(HttpServletRequest request, HttpStatus httpStatus, HttpServletResponse response,
			Exception e) throws IOException {
		response.setStatus(httpStatus.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		ErrorResponse errorResponse = new ErrorResponse(httpStatus.value(), e.getMessage(), request.getRequestURI());
		byte[] body = new ObjectMapper().writeValueAsBytes(errorResponse);

		response.getOutputStream().write(body);
		response.flushBuffer();

	}
}
