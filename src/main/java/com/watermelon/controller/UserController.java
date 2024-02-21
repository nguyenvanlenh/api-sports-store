package com.watermelon.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.watermelon.viewandmodel.request.RequestUser;

@RestController
@RequestMapping("/api/users")
public class UserController {
	
	@GetMapping
	public void printRequest(@RequestBody RequestUser user) {
		System.out.println(user.toString());
	}
	

	

    
}
