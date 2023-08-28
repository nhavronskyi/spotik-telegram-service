package com.example.spotiktelegramservice.component;

import com.example.spotiktelegramservice.props.TelegramProps;
import com.example.spotiktelegramservice.service.BotService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@RequiredArgsConstructor
public class Bot extends TelegramLongPollingBot {
    private final TelegramProps telegramProps;
    private final BotService service;

    @Override
    public String getBotUsername() {
        return telegramProps.username();
    }

    @Override
    public String getBotToken() {
        return telegramProps.token();
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        String message = update.getMessage().getText();
        service.setChatId(String.valueOf(update.getMessage().getChatId()));
        if (message.equals("/start")) {
            execute(service.sendMessage("Welcome to Spotik"));
            execute(service.sendQueue());
        }
    }
}
