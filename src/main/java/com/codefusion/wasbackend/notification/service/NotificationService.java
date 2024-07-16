package com.codefusion.wasbackend.notification.service;

import com.codefusion.wasbackend.exception.ResourceNotFoundException;
import com.codefusion.wasbackend.notification.dto.NotificationDTO;
import com.codefusion.wasbackend.notification.mapper.NotificationMapper;
import com.codefusion.wasbackend.notification.model.NotificationEntity;
import com.codefusion.wasbackend.notification.repository.NotificationRepository;
import com.codefusion.wasbackend.user.model.UserEntity;
import com.codefusion.wasbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationMapper notificationMapper;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    /**
     * Retrieves a list of all non-deleted notifications.
     *
     * @return a List of NotificationDTO objects
     */
    @Transactional(readOnly = true)
    public List<NotificationDTO> getAllNotifications() {
        return notificationRepository.findByIsDeleted(false)
                .stream()
                .map(notificationMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a NotificationDTO object by its ID.
     *
     * @param id the ID of the notification
     * @return a NotificationDTO object representing the notification with the given ID
     * @throws ResourceNotFoundException if no notification is found with the given ID
     */
    @Transactional(readOnly = true)
    public NotificationDTO getNotificationById(Long id) {
        NotificationEntity notification = notificationRepository.findByIdAndIsDeleted(id, false)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + id));

        return notificationMapper.toDto(notification);
    }

    @Transactional
    public NotificationDTO markNotificationIsSeen(Long id) {
        NotificationEntity notification = notificationRepository.findByIdAndIsDeleted(id, false)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + id));

        notification.setIsSeen(true);
        notificationRepository.save(notification);

        return notificationMapper.toDto(notification);
    }

    /**
     * Retrieves a list of notifications for a specific user.
     *
     * @param userId the ID of the user
     * @return a List of NotificationDTO objects representing the notifications for the user
     * @throws ResourceNotFoundException if no user entity is found for the given ID
     */
    @Transactional(readOnly = true)
    public List<NotificationDTO> getNotificationsByUser(Long userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("UserEntity not found for id: " + userId));
        return notificationRepository.findByUserAndIsDeleted(userEntity, false)
                .stream()
                .map(notificationMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Creates a new notification.
     *
     * @param notificationDto the {@link NotificationDTO} object representing the notification
     * @return the newly created {@link NotificationDTO} object
     */
    @Transactional
    public NotificationDTO createNotification(NotificationDTO notificationDto) {
        NotificationEntity notification = notificationMapper.toEntity(notificationDto);
        NotificationEntity savedNotification = notificationRepository.save(notification);
        return notificationMapper.toDto(savedNotification);
    }

    /**
     * Updates an existing notification with the specified id.
     *
     * @param id              the id of the notification to update
     * @param notificationDto the updated notification information
     * @return the updated NotificationDTO object
     * @throws ResourceNotFoundException if the notification with the specified id is not found
     */
    @Transactional
    public NotificationDTO updateNotification(Long id, NotificationDTO notificationDto) {
        NotificationEntity existingNotification = notificationRepository.findByIdAndIsDeleted(id, false)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + id));

        NotificationEntity notification = notificationMapper.toEntity(notificationDto);
        notification.setId(id);
        notification.setIsDeleted(existingNotification.getIsDeleted());
        NotificationEntity savedNotification = notificationRepository.save(notification);
        return notificationMapper.toDto(savedNotification);
    }

    /**
     * Marks a notification as deleted by setting the isDeleted flag to true.
     * If the notification is not found with the given ID, a ResourceNotFoundException is thrown.
     *
     * @param id the ID of the notification to mark as deleted
     * @throws ResourceNotFoundException if the notification is not found with the given ID
     * @Transactional annotates the method to run within a transactional context.
     * This ensures that the changes made to the notification entity are persisted in the database.
     */
    @Transactional
    public void markAsDeleted(Long id) {
        NotificationEntity notification = notificationRepository.findByIdAndIsDeleted(id, false)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + id));

        notification.setIsDeleted(true);
        notificationRepository.save(notification);
    }

    @Transactional(readOnly = true)
    public List<NotificationDTO> getTop3NotificationsByUser(Long userId) {
        Pageable pageable = PageRequest.of(0, 3); // İlk 3 kaydı almak için sayfalama
        return notificationRepository.findTop3ByUserIdOrderByRecordDateDesc(userId, pageable)
                .stream()
                .map(notificationMapper::toDto)
                .collect(Collectors.toList());
    }
}