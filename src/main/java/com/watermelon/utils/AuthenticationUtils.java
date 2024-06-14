package com.watermelon.utils;

import org.springframework.security.core.Authentication;
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
}
