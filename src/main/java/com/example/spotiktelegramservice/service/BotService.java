package com.example.spotiktelegramservice.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface BotService {
    void setChatId(String chatId);

    SendMessage sendMessage(String str);

    SendMessage sendMessage(String str, String chatId);

    SendMessage activate();

    SendMessage deactivate();

    boolean check(String ans);

    SendMessage sendEmail(String message);
}
