package com.watermelon.service;

import com.watermelon.model.request.RequestForgotPassword;
import com.watermelon.model.request.RequestLogin;
import com.watermelon.model.request.RequestRegister;
import com.watermelon.model.response.ResponseForgotPassword;
import com.watermelon.model.response.ResponseLogin;
import com.watermelon.model.response.ResponseRegister;

public interface AuthService {

	ResponseLogin login(RequestLogin request);
	ResponseRegister register(RequestRegister request);
	ResponseForgotPassword forgotPassword(RequestForgotPassword request);
	
}
