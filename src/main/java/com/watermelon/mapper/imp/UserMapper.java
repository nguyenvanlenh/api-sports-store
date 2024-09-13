package com.watermelon.mapper.imp;


import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.watermelon.dto.response.UserResponse;
import com.watermelon.mapper.EntityMapper;
import com.watermelon.model.entity.User;
@Component
public class UserMapper implements EntityMapper<UserResponse, User> {
	
	@Override
	public UserResponse toDTO(User entity) {
		if (ObjectUtils.isEmpty(entity))return null;
		return UserResponse.builder()
				.username(entity.getUsername())
				.firstName(entity.getFirstName())
				.lastName(entity.getLastName())
				.email(entity.getEmail())
				.phone(entity.getPhone())
				.avatar(entity.getAvatar())
				.build();
	}

}
