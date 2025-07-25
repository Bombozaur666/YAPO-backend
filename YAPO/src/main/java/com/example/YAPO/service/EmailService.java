package com.example.YAPO.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String mailFrom;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendHtmlEmail(String to, String subject, String htmlBody) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        helper.setFrom(mailFrom);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);

        mailSender.send(mimeMessage);
    }

    public void sendConfirmationEmail(@Email @NotNull @NotBlank String email, String subject, String link) throws MessagingException {
        String htmlTemplate = link;
        sendHtmlEmail(email, subject, htmlTemplate);
    }
}
