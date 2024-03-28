package com.watermelon.service;

import com.watermelon.model.entity.User;

public interface UserService {
	User findByUsername(String username);
	boolean existsByUsername(String username);
	boolean existsByEmail(String email);
	User saveOrUpdate(User user);
	void saveUserVerificationToken(User theUser, String token);
}
