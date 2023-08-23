package com.example.spotiktelegramservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Setter
public class BotService {
    private String chatId;

    private String message;

    @RabbitListener(queues = {"${rabbitmq.queue}"})
    private void lastMessage(String message) {
        this.message = message;
    }

    public SendMessage sendQueue() {
        try {
            return sendMessage(Optional.ofNullable(message)
                    .map(x -> {
                        try {
                            return new ObjectMapper().readValue(x, IdArtistSongs.class).toString();
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

    @AllArgsConstructor
    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    private static class IdArtistSongs {
        private String id;
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
