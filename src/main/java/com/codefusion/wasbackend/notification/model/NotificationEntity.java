package com.codefusion.wasbackend.notification.model;


import com.codefusion.wasbackend.user.model.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "notification")
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @Column(name = "record_date", columnDefinition = "TIMESTAMP")
    private Instant recordDate;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String text;

    private Boolean isSent = false;

    private Boolean isSeen = false;

    private Long telegramId;

    private Boolean isTelegram;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ElementCollection(targetClass = NotificationLevel.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "notification_level", joinColumns = @JoinColumn(name = "notification_id"))
    @Column(name = "notification_level")
    private Set<NotificationLevel> notificationLevel;

    @PrePersist
    protected void onCreate() {
        recordDate = Instant.now();
    }
}