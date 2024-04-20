package com.watermelon.utils;

public class Constants {

	public static final int QUANTITY_PRODUCT_MAX_BUY = 20;
	public static final int EXPIRATION_TIME_MINUTE = 15;
	
	public static final String ACCESS_TOKEN = "accessToken";
	public static final String REFRESH_TOKEN = "refreshToken";
	
	public static class EmailVerificationMessage{
		public static String EMAIL_NOTIFY_VALID_TOKEN = "valid";
		public static String EMAIL_NOTIFY_INVALID_TOKEN = "Invalid verification token";
		public static String EMAIL_NOTIFY_TOKEN_EXPIRED = "Token already expired";
		public static String EMAIL_NOTIFY_TOKEN_NOT_FOUND = "Verification token not found";
		public static String EMAIL_NOTIFY_ACCOUNT_ALREADY_VERIFIED = "This account has already been verified, please, login.";
		public static String EMAIL_NOTIFY_SUCCESSFULLY_VERIFIED = "Email verified successfully. Now you can login to your account";
	}
	
}
