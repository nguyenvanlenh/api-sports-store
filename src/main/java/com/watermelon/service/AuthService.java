package com.watermelon.service;

import com.watermelon.model.dto.request.ForgotPasswordRequest;
import com.watermelon.model.dto.request.LoginRequest;
import com.watermelon.model.dto.request.RegisterRequest;
import com.watermelon.model.dto.request.ChangePasswordRequest;
import com.watermelon.model.dto.response.LoginResponse;
import com.watermelon.model.entity.User;

public interface AuthService {

	LoginResponse login(LoginRequest request);
	User register(RegisterRequest request);
	String forgotPassword(ForgotPasswordRequest request);
	String changePassword(ChangePasswordRequest request);
	String verifyEmail(String token);
}
