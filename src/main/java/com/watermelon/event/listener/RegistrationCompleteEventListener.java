package com.watermelon.event.listener;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.watermelon.event.RegistrationCompleteEvent;
import com.watermelon.model.entity.User;
import com.watermelon.service.EmailService;
import com.watermelon.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {

    private final UserService userService;
    private final EmailService emailService;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        User theUser = event.getUser();
        String verificationToken = UUID.randomUUID().toString();
        userService.saveUserVerificationToken(theUser, verificationToken);
        String url = event.getApplicationUrl() + "/api/auth/verify-email?token=" + verificationToken;

        Map<String, Object> model = new HashMap<>();
        model.put("user", theUser);
        model.put("url", url);

        emailService.sendEmail(theUser.getEmail(), "Email Verification", "mails/activationEmail", model);

        log.info("Click the link to verify your registration :  {}", url);
    }
}
