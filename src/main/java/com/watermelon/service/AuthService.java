package com.watermelon.service;

import com.watermelon.dto.request.ChangePasswordRequest;
import com.watermelon.dto.request.ForgotPasswordRequest;
import com.watermelon.dto.request.LoginRequest;
import com.watermelon.dto.request.RefreshRequest;
import com.watermelon.dto.request.RegisterRequest;
import com.watermelon.dto.response.TokenResponse;
import com.watermelon.model.entity.User;

public interface AuthService {

	TokenResponse login(LoginRequest request);
	User register(RegisterRequest request);
	String forgotPassword(ForgotPasswordRequest request);
	String changePassword(ChangePasswordRequest request);
	String verifyEmail(String token);
	TokenResponse getAccessTokenFromRefeshToken(RefreshRequest request);
	
}
