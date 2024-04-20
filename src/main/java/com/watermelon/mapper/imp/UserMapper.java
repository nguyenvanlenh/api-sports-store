package com.watermelon.mapper.imp;

import java.util.Optional;

import com.watermelon.dto.UserDTO;
import com.watermelon.mapper.EntityMapper;
import com.watermelon.model.entity.User;

public class UserMapper implements EntityMapper<UserDTO, User>{

	@Override
	public UserDTO toDTO(User entity) {
	    return Optional.ofNullable(entity)
	            .map(user -> new UserDTO(
	                    user.getUsername(),
	                    user.getFirstName(),
	                    user.getLastName(),
	                    user.getEmail(),
	                    user.getPhone(),
	                    user.getAvatar())
	            )
	            .orElse(null);
	}

}
