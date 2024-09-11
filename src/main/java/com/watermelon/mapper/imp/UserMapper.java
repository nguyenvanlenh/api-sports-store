package com.watermelon.mapper.imp;


import org.springframework.util.ObjectUtils;

import com.watermelon.dto.UserDTO;
import com.watermelon.dto.response.OrderResponse.UserResponse;
import com.watermelon.mapper.EntityMapper;
import com.watermelon.model.entity.User;

public class UserMapper implements EntityMapper<UserDTO, User> {
	
	private static UserMapper INSTANCE;

	private UserMapper() {
	}

	public static UserMapper getInstance() {
		if (INSTANCE == null)
			INSTANCE = new UserMapper();
		return INSTANCE;
	}
	
	@Override
	public UserDTO toDTO(User entity) {
		if (ObjectUtils.isEmpty(entity))return null;
		return new UserDTO(
				entity.getUsername(),
				entity.getFirstName(),
				entity.getLastName(),
				entity.getEmail(),
				entity.getPhone(),
				entity.getAvatar());
	}

	public UserResponse toResponse(User entity) {
		if (ObjectUtils.isEmpty(entity))return null;
		return new UserResponse(entity.getId(), entity.getUsername());
	}

}
