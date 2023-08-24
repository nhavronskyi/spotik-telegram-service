package com.example.spotiktelegramservice.dao;

import com.example.spotiktelegramservice.entity.TelegramUser;

public interface TelegramUserDao {
    void save(TelegramUser user);

    TelegramUser findById(String chatId);

    void activeStatus(String chatId, boolean status);
}
