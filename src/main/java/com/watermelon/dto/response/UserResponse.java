package com.watermelon.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserResponse(
		Long id,
		String username,
		String firstName,
		String lastName,
		String email,
		String phone,
		String avatar,
		boolean isActive,
		java.util.List<RoleResponse> listRoles){
}
