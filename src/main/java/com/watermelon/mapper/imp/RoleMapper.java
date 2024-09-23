package com.watermelon.mapper.imp;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import com.watermelon.dto.response.RoleResponse;
import com.watermelon.model.entity.User;
@Component
public class RoleMapper {

	public List<RoleResponse> toDTO(User entity) {
	    return Optional.ofNullable(entity)
	    		.map(user -> user.getListRoles().stream()
		            .flatMap(role -> {
		                Stream<RoleResponse> roleStream = Stream.of(
		                        RoleResponse.builder().name("ROLE_" + role.getName()).build()
		                );
		                Stream<RoleResponse> permissionStream = role.getListPermissions().stream()
		                        .map(per -> RoleResponse.builder().name(per.getName()).build());
		                return Stream.concat(roleStream, permissionStream);
		            })
		            .toList())
	    		.orElse(Collections.emptyList());
	}


}
