package com.watermelon.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.data.web.SortDefault.SortDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.watermelon.dto.response.PageResponse;
import com.watermelon.dto.response.ResponseData;
import com.watermelon.dto.response.UserResponse;
import com.watermelon.service.UserService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

	UserService userService;
	
	@GetMapping("/{id}")
	public ResponseData<UserResponse> getUser(@PathVariable(name="id") Long id){
		UserResponse data = userService.getUserById(id);
		return ResponseData.<UserResponse>builder()
				.status(HttpStatus.OK.value())
				.message("User data")
				.data(data)
				.build();
	}
	@GetMapping
	public ResponseData<PageResponse<List<UserResponse>>> getAllUsers(
			@PageableDefault(page = 0, size = 20)
			@SortDefaults(@SortDefault(
							direction = Sort.Direction.DESC,
							sort = "id")) Pageable pageable){
		PageResponse<List<UserResponse>> data = userService.getAllUsers(pageable);
		return ResponseData.<PageResponse<List<UserResponse>>>builder()
				.status(HttpStatus.OK.value())
				.message("Users data")
				.data(data)
				.build();
	}
	@PatchMapping("/{idUser}")
	public ResponseData<Void> updateUserActive(
			@PathVariable(name = "idUser") Long idUser,
			@RequestParam(name = "active") Boolean active){
		userService.updateStatusUser(idUser, active);
		return ResponseData.<Void>builder()
				.status(HttpStatus.ACCEPTED.value())
				.message("User updated successfully")
				.build();
	}
}
