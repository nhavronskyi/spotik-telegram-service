package com.example.spotiktelegramservice.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface BotService {
    void setChatId(String chatId);

    SendMessage sendQueue();

    SendMessage sendMessage(String str);

    SendMessage activate();

    SendMessage deactivate();

    boolean check(String ans);

    SendMessage sendEmail(String message);
}
