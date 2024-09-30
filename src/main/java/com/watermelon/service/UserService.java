package com.watermelon.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.watermelon.dto.response.PageResponse;
import com.watermelon.dto.response.UserResponse;
import com.watermelon.model.entity.User;

public interface UserService {
	UserResponse findByUsername(String username);
	PageResponse<List<UserResponse>> getAllUsers(Pageable pageable);
	UserResponse getUserById(Long id);
	boolean existsByUsername(String username);
	boolean existsByEmail(String email);
	Long saveOrUpdate(User user);
	void updateUserStatus(Long userId, Boolean status);
	void saveUserVerificationToken(User theUser, String token);
}
