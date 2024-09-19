package com.watermelon.mapper.imp;


import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

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
		if (ObjectUtils.isEmpty(entity))return null;
		return UserResponse.builder()
				.id(entity.getId())
				.username(entity.getUsername())
				.firstName(entity.getFirstName())
				.lastName(entity.getLastName())
				.email(entity.getEmail())
				.phone(entity.getPhone())
				.avatar(entity.getAvatar())
				.isActive(entity.isActive())
				.listRoles(roleMapper.toDTO(entity))
				.build();
	}

}
