package com.codefusion.wasbackend.notification.service;

import com.codefusion.wasbackend.notification.mapper.NotificationMapper;
import com.codefusion.wasbackend.notification.model.NotificationEntity;
import com.codefusion.wasbackend.notification.repository.NotificationRepository;
import com.codefusion.wasbackend.telegramAutomation.service.TelegramBotService;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationTaskService {

    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;
    private final TelegramBotService telegramBotService;
    private final NotificationMapper notificationMapper;

    public NotificationTaskService(NotificationRepository notificationRepository, NotificationService notificationService,
                                   TelegramBotService telegramBotService, NotificationMapper notificationMapper) {
        this.notificationRepository = notificationRepository;
        this.telegramBotService = telegramBotService;
        this.notificationService = notificationService;
        this.notificationMapper = notificationMapper;
    }

    @Scheduled(fixedRate = 300000)
    @Transactional
    public void sendNotifications() {
        Instant twoHoursAgo = Instant.now().minusSeconds(7200);
        List<NotificationEntity> notifications =
                this.notificationRepository.findNotifications(twoHoursAgo);
        if (notifications != null) {
            for (NotificationEntity notification : notifications) {
                if (notification != null && (notification.getIsSent() == null ||
                        !notification.getIsSent())) {
                    String text = notification.getText() != null ?
                            notification.getText() : "";
                    String description = notification.getDescription() != null ?
                            notification.getDescription() : "";
                    String subject = notification.getSubject() != null ?
                            notification.getSubject() : "";
                    Long telegramId = notification.getTelegramId();

                    if (telegramId != null) {
                        telegramBotService.sendMessage(telegramId,
                                subject + "\n" + description + "\n" + text);
                        notification.setIsSent(true);
                    }

                    notificationService.updateNotification(notification.getId(),
                            notificationMapper.toDto(notification));
                }
            }
        }
    }
}