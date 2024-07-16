package com.codefusion.wasbackend;

import com.codefusion.wasbackend.telegramAutomation.service.TelegramBotService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WasBackendApplication {
//    public String PORT = System.getenv("PORT");

    public static void main(String[] args) {
        SpringApplication.run(WasBackendApplication.class, args);
    }

    @Bean
    public CommandLineRunner runner(TelegramBotService telegramService) {
        return args -> {
            try {
                TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
                botsApi.registerBot(telegramService);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        };
    }
}