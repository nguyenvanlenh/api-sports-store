package com.watermelon.dto.mapper.imp;

import java.util.Optional;

import com.watermelon.dto.UserDTO;
import com.watermelon.dto.mapper.EntityMapper;
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
