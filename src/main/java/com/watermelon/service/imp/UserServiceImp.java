package com.watermelon.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.watermelon.model.entity.User;
import com.watermelon.repository.UserRepository;
import com.watermelon.service.UserService;

public class UserServiceImp implements UserService{

	@Autowired
	private UserRepository userRepository;
	@Override
	public User findByUsername(String username) {
		return userRepository.findByUsername(username)
				.orElseThrow(()-> new UsernameNotFoundException("Username not found"));
	}

	@Override
	public boolean existsByUsername(String username) {
		return userRepository.existsByUsername(username);
	}

	@Override
	public boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}

	@Override
	public User saveOrUpdate(User user) {
		return userRepository.save(user);
	}

}
