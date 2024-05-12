package com.watermelon.security.jwt;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.watermelon.exception.UserNotActivatedException;
import com.watermelon.security.CustomUserDetails;
import com.watermelon.utils.Constants;

import static com.watermelon.utils.Constants.EndPoint.PUBLIC_ENDPOINTS;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	JwtTokenProvider jwtTokenProvider;

	UserDetailsService userDetailsService;

	HandlerExceptionResolver exceptionResolver;

	static final String TYPE_AUTHORIZATION = "Bearer ";
	

	public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider,
			UserDetailsService userDetailsService,
			@Qualifier("handlerExceptionResolver") HandlerExceptionResolver exceptionResolver) {
		this.jwtTokenProvider = jwtTokenProvider;
		this.userDetailsService = userDetailsService;
		this.exceptionResolver = exceptionResolver;
	}

	private boolean isPublicEndpoint(HttpServletRequest request) {
		return Arrays.stream(PUBLIC_ENDPOINTS).anyMatch(e -> request.getRequestURI().matches(e));
	}
	
	private String getJwtFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TYPE_AUTHORIZATION)) {
			return bearerToken.substring(TYPE_AUTHORIZATION.length());
		}
		return null;
	}

	private boolean validateJwt(String jwt) {
		 return StringUtils.hasText(jwt) 
		           && jwtTokenProvider.validateToken(jwt) 
		           && Constants.ACCESS_TOKEN.equals(jwtTokenProvider.getTypeToken(jwt));
	}
	

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			String jwt = getJwtFromRequest(request);
			if (!isPublicEndpoint(request) && validateJwt(jwt)) {
				String username = jwtTokenProvider.getUsernameFromToken(jwt);

				CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(username);
				
				if(!userDetails.isActive())
					throw new UserNotActivatedException("User not active");

				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());

				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication(authentication);
			}

			filterChain.doFilter(request, response);
		} catch (
				ExpiredJwtException | 
				SignatureException |
				MalformedJwtException | 
				NoHandlerFoundException |
				UserNotActivatedException |
				UsernameNotFoundException ex) {
			exceptionResolver.resolveException(request, response, null, ex);
			log.error(ex.getMessage());
		}

	}

}
