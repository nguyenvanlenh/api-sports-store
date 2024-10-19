package com.watermelon.service;

import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.watermelon.exception.EmailMessagingException;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String sender;

    @Async
    public void sendEmail(String to, String subject, String templateName, Map<String, Object> model) {
        try {
            String content = generateEmailContent(templateName, model);
            sendMail(to, subject, content);
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("Failed to send email to '{}': {}", to, e.getMessage());
            throw new EmailMessagingException("Failed to send email");
        }
    }

    private String generateEmailContent(String templateName, Map<String, Object> model) {
        Context context = new Context(Locale.getDefault(), model);
        return templateEngine.process(templateName, context);
    }

    private void sendMail(String to, String subject, String content) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
        messageHelper.setFrom(sender);
        messageHelper.setTo(to);
        messageHelper.setSubject(subject);
        messageHelper.setText(content, true);
        mailSender.send(message);
        log.info("Email sent successfully to '{}'", to);
    }
}
