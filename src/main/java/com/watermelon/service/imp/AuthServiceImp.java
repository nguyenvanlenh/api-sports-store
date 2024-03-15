package com.watermelon.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.watermelon.model.request.RequestForgotPassword;
import com.watermelon.model.request.RequestLogin;
import com.watermelon.model.request.RequestRegister;
import com.watermelon.model.response.ResponseForgotPassword;
import com.watermelon.model.response.ResponseLogin;
import com.watermelon.model.response.ResponseRegister;
import com.watermelon.repository.UserRepository;
import com.watermelon.service.AuthService;
@Service
public class AuthServiceImp implements AuthService{

	@Autowired
	private UserRepository userRepository;
	@Override
	public ResponseLogin login(RequestLogin request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseRegister register(RequestRegister request) {
		ResponseRegister rs = null;
		if(!request.password().equals(request.repeatPassword())) {
			return rs;
		}
		return rs;
	}

	@Override
	public ResponseForgotPassword forgotPassword(RequestForgotPassword request) {
		ResponseForgotPassword rs = null;
		if(userRepository.findByUsernameAndEmailAndPhone(
				request.username(),
				request.email(),
				request.phone())
				== null)
			return rs;
		
		return rs;
	}
	
	
	

}
