package com.watermelon.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LoginResponse(
		String jwt,
		boolean authenticated,
		@JsonProperty("list_roles")
		java.util.Set<String> listRoles){

}
