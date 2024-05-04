package com.watermelon.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.watermelon.dto.UserDTO;
import com.watermelon.dto.response.PageResponse;
import com.watermelon.model.entity.User;

public interface UserService {
	UserDTO findByUsername(String username);
	PageResponse<List<UserDTO>> getAllUsers(Pageable pageable);
	UserDTO getUserById(Long id);
	boolean existsByUsername(String username);
	boolean existsByEmail(String email);
	Long saveOrUpdate(User user);
	void updateStatusUser(Long idUser, Boolean active);
	void saveUserVerificationToken(User theUser, String token);
}
