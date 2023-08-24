package com.example.spotiktelegramservice.service.impl;

import com.example.spotiktelegramservice.service.EmailSender;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailSenderImpl implements EmailSender {
    private final JavaMailSender mailSender;

    @SneakyThrows
    @Override
    public void sendEmail(String toEmail, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("medivalqrcodesite@gmail.com");
        message.setTo(toEmail);
        message.setSubject("authorization code for spotik-bot");
        message.setText(body);
        mailSender.send(message);
    }
}
