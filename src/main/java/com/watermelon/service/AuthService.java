package com.watermelon.service;

import com.watermelon.viewandmodel.error.ResponseForgotPassword;
import com.watermelon.viewandmodel.error.ResponseLogin;
import com.watermelon.viewandmodel.error.ResponseRegister;
import com.watermelon.viewandmodel.request.RequestForgotPassword;
import com.watermelon.viewandmodel.request.RequestLogin;
import com.watermelon.viewandmodel.request.RequestRegister;

public interface AuthService {

	ResponseLogin login(RequestLogin request);
	ResponseRegister register(RequestRegister request);
	ResponseForgotPassword forgotPassword(RequestForgotPassword request);
	
}
