package com.watermelon.service;

import com.watermelon.dto.request.ForgotPasswordRequest;
import com.watermelon.dto.request.LoginRequest;
import com.watermelon.dto.request.RefreshRequest;
import com.watermelon.dto.request.RegisterRequest;
import com.watermelon.dto.request.UpdatePasswordRequest;
import com.watermelon.dto.response.AuthenticationResponse;
import com.watermelon.model.entity.User;
import com.watermelon.model.enumeration.EDevice;

public interface AuthService {

	AuthenticationResponse outboundAuthenticate(String code, String type,EDevice device);
	AuthenticationResponse login(LoginRequest request,EDevice device);
	User register(RegisterRequest request);
	void logout(EDevice device);
	void forgotPassword(ForgotPasswordRequest request);
	void updatePassword(UpdatePasswordRequest request);
	String verifyEmail(String token);
	AuthenticationResponse getAccessTokenFromRefeshToken(RefreshRequest request);
	boolean revokeRefreshToken(RefreshRequest request);
}
