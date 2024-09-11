package com.watermelon.dto.response;
import java.io.Serializable;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.watermelon.model.enumeration.ETypeAccount;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthenticationResponse implements Serializable {

	private static final long serialVersionUID = 1L;
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String avatar;
    private String accessToken;
    private String refreshToken;
    private ETypeAccount typeAccount;
    private boolean hasPassword;
    private Set<String> listRoles;
}