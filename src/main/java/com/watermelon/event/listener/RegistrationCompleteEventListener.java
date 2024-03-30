package com.watermelon.event.listener;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.watermelon.event.RegistrationCompleteEvent;
import com.watermelon.model.entity.User;
import com.watermelon.service.UserService;
import com.watermelon.utils.Constants;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {

	JavaMailSender mailSender;
	UserService userService;
	@NonFinal
	User theUser;

	@Async
	@Override
	public void onApplicationEvent(RegistrationCompleteEvent event) {
		// 1. Get the newly registered user
		theUser = event.getUser();
		// 2. Create a verification token for the user
		String verificationToken = UUID.randomUUID().toString();
		// 3. Save the verification token for the user
		userService.saveUserVerificationToken(theUser, verificationToken);
		// 4 Build the verification url to be sent to the user
		String url = event.getApplicationUrl() + "/api/auth/verifyEmail?token=" + verificationToken;
		// 5. Send the email.
		try {
			sendVerificationEmail(url);
		} catch (MessagingException | UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		log.info("Click the link to verify your registration :  {}", url);

	}

	public void sendVerificationEmail(String url) throws MessagingException, UnsupportedEncodingException {
		String subject = "Email Verification";
		String senderName = "User Registration Portal Service";

		// Constructing HTML content using Bootstrap for better styling
		String mailContent = "<div class='container'><div class='row'><div class='col-md-6 offset-md-3'>";
		mailContent += "<div class='card'><div class='card-body'>";
		mailContent += "<h5 class='card-title'>Hi, " + theUser.getUsername() + "</h5>";
		mailContent += "<p class='card-text'>Thank you for registering with us. Please follow the link below to complete your registration:</p>";
		mailContent += "<a href='" + url + "' class='btn btn-primary'>Verify your email to activate your account</a>";
		mailContent += "<p class='card-text'>Please note that this verification link will expire in "
				+ Constants.EXPIRATION_TIME_MINUTE + " minutes.</p>";
		mailContent += "</div></div></div></div></div>";

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
		messageHelper.setFrom("20130303@st.hcmuaf.edu.vn", senderName);
		messageHelper.setTo(theUser.getEmail());
		messageHelper.setSubject(subject);
		messageHelper.setText(mailContent, true);
		mailSender.send(message);
	}

}
