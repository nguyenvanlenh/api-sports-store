package com.watermelon.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomErrorController implements ErrorController {

	private static final String PATH = "/error";

	@RequestMapping(value = PATH)
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public String error() {
		return "404 not found";
	}

	public String getErrorPath() {
		return PATH;
	}
	

}
