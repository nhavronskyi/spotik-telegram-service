package com.example.spotiktelegramservice.service;

public interface EmailSender {
    void sendEmail(String toEmail, String body);
}
