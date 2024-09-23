package com.watermelon.mapper.imp;


import java.util.Optional;

import org.springframework.stereotype.Component;

import com.watermelon.dto.response.UserResponse;
import com.watermelon.mapper.EntityMapper;
import com.watermelon.model.entity.User;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserMapper implements EntityMapper<UserResponse, User> {
	RoleMapper roleMapper;
	
	@Override
	public UserResponse toDTO(User entity) {
		return Optional.ofNullable(entity)
				.map(user -> UserResponse.builder()
					.id(entity.getId())
					.username(entity.getUsername())
					.firstName(entity.getFirstName())
					.lastName(entity.getLastName())
					.email(entity.getEmail())
					.phone(entity.getPhone())
					.avatar(entity.getAvatar())
					.isActive(entity.isActive())
					.listRoles(roleMapper.toDTO(entity))
					.build())
				.orElse(UserResponse.builder().build());
	}

}
