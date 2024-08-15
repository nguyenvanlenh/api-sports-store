package com.watermelon.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.watermelon.utils.Constants.EmailVerificationMessage;
import com.watermelon.service.AuthService;

@Controller
@RequestMapping("/api/auth")
public class EmailController {

	private final AuthService authService;

	public EmailController(AuthService authService) {
		this.authService = authService;
	}

	@GetMapping("/verify-email")
	public String verifyEmail(@RequestParam("token") String token) {
		String verificationResult = authService.verifyEmail(token);

		switch (verificationResult) {
		case EmailVerificationMessage.EMAIL_NOTIFY_SUCCESSFULLY_VERIFIED:
			return "mails/success";
		case EmailVerificationMessage.EMAIL_NOTIFY_ACCOUNT_ALREADY_VERIFIED:
			return "mails/alreadyVerified";
		case EmailVerificationMessage.EMAIL_NOTIFY_INVALID_TOKEN:
			return "mails/invalidToken";
		case EmailVerificationMessage.EMAIL_NOTIFY_TOKEN_EXPIRED:
			return "mails/expiredToken";
		default:
			return "mails/invalidToken";
		}
	}
}
