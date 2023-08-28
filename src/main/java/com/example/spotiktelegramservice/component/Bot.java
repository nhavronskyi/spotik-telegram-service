package com.example.spotiktelegramservice.component;

import com.example.spotiktelegramservice.dao.TelegramUserDao;
import com.example.spotiktelegramservice.props.TelegramProps;
import com.example.spotiktelegramservice.service.BotService;
import com.example.spotiktelegramservice.service.SendingService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@RequiredArgsConstructor
public class Bot extends TelegramLongPollingBot {
    private final TelegramProps telegramProps;
    private final BotService botService;
    private final SendingService sendingService;
    private final TelegramUserDao userDao;

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
        botService.setChatId(String.valueOf(update.getMessage().getChatId()));
        switch (message) {
            case "/start" -> execute(botService.sendMessage("Welcome to Spotik\n"
                    + "next step is activation sending releases, please put /activate {your spotify email}"));
            case "/deactivate" -> execute(botService.deactivate());
        }

        if (message.startsWith("/activate")) {
            execute(botService.sendEmail(message));
        } else if (message.startsWith("/check")) {
            execute(botService.check(message) ? botService.activate()
                    : botService.sendMessage("invalid code"));
        }
    }

    @Scheduled(cron = "*/5 * * * * *")
    public void showReleases() {
        userDao.getAllUsersWithActiveStatus()
                .forEach(user -> sendApiMethodAsync(sendingService.sendQueue(user.getEmail(), user.getId())));
    }
}
