package com.example.spotiktelegramservice.service.impl;

import com.example.spotiktelegramservice.dao.TelegramUserDao;
import com.example.spotiktelegramservice.entity.TelegramUser;
import com.example.spotiktelegramservice.service.BotService;
import com.example.spotiktelegramservice.service.EmailSender;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class BotServiceImpl implements BotService {
    private final EmailSender sender;
    private final TelegramUserDao telegramUserDao;
    private String chatId;
    private String message;

    @RabbitListener(queues = {"${rabbitmq.queue}"})
    private void lastMessage(String message) {
        this.message = message;
    }

    @Override
    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public SendMessage sendQueue() {
        try {
            return sendMessage(Optional.ofNullable(message)
                    .map(x -> {
                        try {
                            return new ObjectMapper().readValue(x, EmailArtistSongs.class).toString();
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .orElse("There are no new releases"));
        } finally {
            message = null;
        }
    }

    public SendMessage sendMessage(String str) {
        return new SendMessage(chatId, str);
    }

    public SendMessage sendEmail(String message) {
        try {
            var email = message.split(" ")[1];
            int num = validationFourNumbers();
            telegramUserDao.save(new TelegramUser(chatId, email, false, num));
            sender.sendEmail(email, "Your authorization code is: " + num);
            return sendMessage("please put code from mail /check {code}");
        } catch (Exception e) {
            return sendMessage("email isn't correct");
        }
    }

    public boolean check(String ans) {
        try {
            var user = telegramUserDao.findById(chatId);
            return user.getCheckNumber().equals(Integer.parseInt(ans.split(" ")[1]));
        } catch (Exception e) {
            return false;
        }
    }

    public SendMessage activate() {
        telegramUserDao.activeStatus(chatId, true);
        return sendMessage("You have activated sending your last releases from Spotify\n"
                + "you can deactivate sending releases with /deactivate");
    }

    public SendMessage deactivate() {
        telegramUserDao.activeStatus(chatId, false);
        return sendMessage("sending stopped");
    }

    private int validationFourNumbers() {
        int min = 1000;
        int max = 9999;
        return new Random().nextInt(max - min + 1) + min;
    }

    @AllArgsConstructor
    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    private static class EmailArtistSongs {
        private String email;
        private List<ArtistSongs> artists;
    }

    @AllArgsConstructor
    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    private static class ArtistSongs {
        private String artist;
        private List<String> songs;
    }
}
