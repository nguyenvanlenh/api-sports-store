package com.watermelon.event.listener;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.watermelon.event.RegistrationCompleteEvent;
import com.watermelon.exception.EmailMessagingException;
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
	TemplateEngine templateEngine;

	
	@NonFinal
	@Value("${spring.mail.username}")
	String sender;

	@Async
	@Override
	public void onApplicationEvent(RegistrationCompleteEvent event) {
		User theUser = event.getUser();
		String verificationToken = UUID.randomUUID().toString();
		userService.saveUserVerificationToken(theUser, verificationToken);
		String url = event.getApplicationUrl() + "/api/auth/verify-email?token=" + verificationToken;
		try {
			sendActivationEmail(theUser , url);
		} catch (MessagingException | UnsupportedEncodingException e) {
			throw new EmailMessagingException(e.getMessage());
		}
		log.info("Click the link to verify your registration :  {}", url);

	}
	public void sendActivationEmail(User user,String url) throws UnsupportedEncodingException, MessagingException {
		log.debug("Sending activation email to '{}'", user.getEmail());
		String subject = "Email Verification";
		String senderName = "User Registration Portal Service";
		Map<String, Object> model = new HashMap<>();
		model.put("user", user);
		model.put("url", url);
		model.put("time", Constants.EXPIRATION_TIME_MINUTE);
		sendEmailFromTemplate(user, "mails/activationEmail",subject, senderName , model);
	}

	public void sendEmailFromTemplate(User user , String templateName, String subject, String senderName, Map<String, Object> model) throws MessagingException, UnsupportedEncodingException {
		String content = templateEngine.process(templateName,new Context(Locale.getDefault(), model));
		sendMail(senderName, user.getEmail(), subject, content, false, true);

		
	}
	public void sendMail(String senderName, String to, String subject, String content,boolean isMultipart,boolean isHtml ) throws MessagingException, UnsupportedEncodingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper(message, isMultipart, "UTF-8");
		messageHelper.setFrom(sender, senderName);
		messageHelper.setTo(to);
		messageHelper.setSubject(subject);
		messageHelper.setText(content, isHtml);
		mailSender.send(message);
	}
	


}
