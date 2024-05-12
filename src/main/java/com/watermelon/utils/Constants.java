package com.watermelon.utils;

public class Constants {
	
	private Constants(){}
	public static final int QUANTITY_PRODUCT_MAX_BUY = 20;
	public static final int EXPIRATION_TIME_MINUTE = 15;
	
	public static final String ACCESS_TOKEN = "accessToken";
	public static final String REFRESH_TOKEN = "refreshToken";
	
	public static class EmailVerificationMessage{
		private EmailVerificationMessage(){}
		public static final String EMAIL_NOTIFY_VALID_TOKEN = "valid";
		public static final String EMAIL_NOTIFY_INVALID_TOKEN = "Invalid verification token";
		public static final String EMAIL_NOTIFY_TOKEN_EXPIRED = "Token already expired";
		public static final String EMAIL_NOTIFY_TOKEN_NOT_FOUND = "Verification token not found";
		public static final String EMAIL_NOTIFY_ACCOUNT_ALREADY_VERIFIED = "This account has already been verified, please, login.";
		public static final String EMAIL_NOTIFY_SUCCESSFULLY_VERIFIED = "Email verified successfully. Now you can login to your account";
	}
	
	public static class EndPoint{
		private EndPoint() {}
		public static final String[] PUBLIC_ENDPOINTS = {
				"/api/auth/login",
				"/api/auth/register",
				"/api/auth/verifyEmail",
				"/api/auth/refreshToken"
				};

		public static final String[] SWAGGER_ENDPOINTS = {
				"swagger-ui.html",
				"/swagger-ui/**",
				"/v3/api-docs/**",
				"/javainuse-openapi/**" };
	}
	
}
