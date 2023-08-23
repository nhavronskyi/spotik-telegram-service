package com.example.spotiktelegramservice.config;

import com.example.spotiktelegramservice.component.Bot;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
@RequiredArgsConstructor
public class BotConfig {
    private final Bot bot;

    @SneakyThrows
    @Bean
    public void starter() {
        TelegramBotsApi tba = new TelegramBotsApi(DefaultBotSession.class);
        tba.registerBot(bot);
    }
}
