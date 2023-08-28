package com.example.spotiktelegramservice.dao;

import com.example.spotiktelegramservice.entity.TelegramUser;

import java.util.List;

public interface TelegramUserDao {
    void save(TelegramUser user);

    TelegramUser findById(String chatId);

    void activeStatus(String chatId, boolean status);

    List<TelegramUser> getAllUsersWithActiveStatus();
}
