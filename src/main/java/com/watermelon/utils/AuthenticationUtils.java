package com.watermelon.utils;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.watermelon.security.CustomUserDetails;

public class AuthenticationUtils {
	private AuthenticationUtils(){}
	public static Long extractUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		var principal = authentication.getPrincipal();
		if (principal.equals("anonymousUser")) {
			return 0L;
		}
		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		return userDetails.getId();
	}
	 public static List<String> extractUserAuthorities() {
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        if (authentication != null && authentication.isAuthenticated() &&
	        		!(authentication.getPrincipal() instanceof String)) {
	            return authentication.getAuthorities().stream()
	                    .map(GrantedAuthority::getAuthority)
	                    .collect(Collectors.toList());
	        }
	        return List.of();
	    }
}
