package com.watermelon.dto.response;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ResponseSignup {
	private String token;
	private long userId;
	private String userName;
	private String email;
	private String phone;
	private Set<String> listRoles;
}
