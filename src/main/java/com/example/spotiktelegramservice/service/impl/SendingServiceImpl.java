package com.example.spotiktelegramservice.service.impl;

import com.example.spotiktelegramservice.service.BotService;
import com.example.spotiktelegramservice.service.SendingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SendingServiceImpl implements SendingService {
    private final BotService service;
    private final Map<String, List<ArtistSongs>> emailArtistSongsSet = new HashMap<>();

    @SneakyThrows
    @RabbitListener(queues = {"${rabbitmq.queue}"})
    private void lastMessage(String message) {
        var obj = new ObjectMapper().readValue(message, EmailArtistSongs[].class);
        var map = Arrays.stream(obj).collect(Collectors.toMap(x -> x.email, x -> x.artists));
        emailArtistSongsSet.putAll(map);
    }

    @Override
    public SendMessage sendQueue(String email, String chatId) {
        try {
            return service.sendMessage(emailArtistSongsSet.get(email).stream()
                    .map(artistSongs -> artistSongs.artist + " " + String.join(", ", artistSongs.songs))
                    .collect(Collectors.joining("\n")), chatId);
        } catch (Exception e) {
            return service.sendMessage("There are no new releases", chatId);
        }
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
