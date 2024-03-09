package com.watermelon.service;

import com.watermelon.viewandmodel.request.RequestForgotPassword;
import com.watermelon.viewandmodel.request.RequestLogin;
import com.watermelon.viewandmodel.request.RequestRegister;
import com.watermelon.viewandmodel.response.ResponseForgotPassword;
import com.watermelon.viewandmodel.response.ResponseLogin;
import com.watermelon.viewandmodel.response.ResponseRegister;

public interface AuthService {

	ResponseLogin login(RequestLogin request);
	ResponseRegister register(RequestRegister request);
	ResponseForgotPassword forgotPassword(RequestForgotPassword request);
	
}
