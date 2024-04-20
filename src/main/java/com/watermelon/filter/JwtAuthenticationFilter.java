package com.watermelon.filter;

import java.io.IOException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.watermelon.security.jwt.JwtTokenProvider;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtAuthenticationFilter extends OncePerRequestFilter{

	JwtTokenProvider jwtTokenProvider;

	UserDetailsService userDetailsService;
	
	@NonFinal
	private String TYPE_AUTHORIZATION = "Bearer ";

	private String getJwtFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TYPE_AUTHORIZATION)) {
			return bearerToken.substring(TYPE_AUTHORIZATION.length());
		}
		return null;
	}

	private boolean validateJwt(String jwt) {
		boolean result = false;
		if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
			result = true;
		}
		return result;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
			String jwt = getJwtFromRequest(request);
			if (validateJwt(jwt)) {
				String username = jwtTokenProvider.getUsernameFromToken(jwt);

				UserDetails userDetails = userDetailsService.loadUserByUsername(username);

				if (userDetails == null) {
					response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: User details not found");
					return;
				}

				// Tạo đối tượng authentication từ UserDetails
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
						null, userDetails.getAuthorities());

				// Thiết lập chi tiết xác thực từ request
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication(authentication);
			}

		filterChain.doFilter(request, response);

	}

}
