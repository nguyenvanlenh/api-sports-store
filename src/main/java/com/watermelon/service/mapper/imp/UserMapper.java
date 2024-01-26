package com.watermelon.service.mapper.imp;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.watermelon.model.entity.User;
import com.watermelon.service.dto.UserDTO;
import com.watermelon.service.mapper.EntityMapper;

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


	

//	@Override
//	public User toEntity(UserDTO dto) {
//	    
//	    return Optional.ofNullable(dto)
//	    		.map(d -> {
//	    			User u = new User();
//	    			 u.setUsername(d.username());
//	                 u.setFirstName(d.firstName());
//	                 u.setLastName(d.lastName());
//	                 u.setEmail(d.email());
//	                 u.setPhone(d.phone());
//	                 u.setAvatar(d.avarta());
//	    			return u;
//	    			})
//	    		.orElse(null);
//	}
//
//
//	@Override
//	public List<User> toEntity(List<UserDTO> dtos) {
//		return Optional
//				.ofNullable(
//						dtos.stream()
//						.map(this::toEntity)
//						.collect(Collectors.toList())
//						)
//				.orElse(null);
//	}
	

}
