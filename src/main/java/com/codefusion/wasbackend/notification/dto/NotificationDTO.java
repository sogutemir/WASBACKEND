package com.codefusion.wasbackend.notification.dto;

import com.codefusion.wasbackend.notification.model.NotificationLevel;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import java.util.Set;

/**
 * DTO for {@link com.codefusion.wasbackend.notification.model.NotificationEntity}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO implements Serializable {

    private Long id;
    private Boolean isDeleted;
    private Boolean isSeen;
    private Instant recordDate;
    private String subject;
    private String description;
    private String text;
    private Boolean isSent;
    private Long telegramId;
    private Boolean isTelegram;
    private UserEntityDto user;
    private Set<NotificationLevel> notificationLevel;

    /**
     * DTO for {@link com.codefusion.wasbackend.user.model.UserEntity}
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserEntityDto implements Serializable {
        private Long id;
        private Boolean isDeleted;
        @NotBlank(message = "Name cannot be empty")
        private String name;
        @NotBlank(message = "Surname cannot be empty")
        private String surname;
        @NotBlank(message = "Email cannot be empty")
        private String email;
        @NotBlank(message = "Phone cannot be empty")
        private String phoneNo;
        private Long telegramId;
        private Boolean isTelegram;
        private String ActivationRequestCode;
        private Date telegramLinkTime;
    }

}