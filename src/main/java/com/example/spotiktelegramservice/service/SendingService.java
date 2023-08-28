package com.example.spotiktelegramservice.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface SendingService {
    SendMessage sendQueue(String email, String chatId);
}
