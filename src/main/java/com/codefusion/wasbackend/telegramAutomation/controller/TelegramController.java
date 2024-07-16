package com.codefusion.wasbackend.telegramAutomation.controller;


import com.codefusion.wasbackend.telegramAutomation.service.TelegramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/telegram")
public class TelegramController {

    private final TelegramService telegramService;

    @Autowired
    public TelegramController(TelegramService telegramService) {
        this.telegramService = telegramService;
    }

    @GetMapping("/linkUser/{userId}")
    public ResponseEntity<String> linkTelegramUser(@PathVariable Long userId) {
        String deepLink = telegramService.linkTelegramUser(userId);
        if (deepLink == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(deepLink);
    }
}