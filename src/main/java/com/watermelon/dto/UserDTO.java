package com.watermelon.dto;

import java.io.Serializable;

public record UserDTO(
		String username,
		String firstName,
		String lastName,
		String email,
		String phone,
		String avarta) implements Serializable{
}
