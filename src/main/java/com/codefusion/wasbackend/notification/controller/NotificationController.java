package com.codefusion.wasbackend.notification.controller;

import com.codefusion.wasbackend.exception.ResourceNotFoundException;
import com.codefusion.wasbackend.notification.dto.NotificationDTO;
import com.codefusion.wasbackend.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * Retrieves a list of all non-deleted notifications.
     *
     * @return a List of NotificationDTO objects representing the notifications
     */
    @GetMapping("/allNotifications")
    public ResponseEntity<List<NotificationDTO>> getAllNotifications() {
        return ResponseEntity.ok(notificationService.getAllNotifications());
    }

    /**
     * Retrieves a NotificationDTO object by its ID.
     *
     * @param id the ID of the notification
     * @return a NotificationDTO object representing the notification with the given ID
     * @throws ResourceNotFoundException if no notification is found with the given ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<NotificationDTO> getNotificationById(@PathVariable Long id) {
        return ResponseEntity.ok(notificationService.getNotificationById(id));
    }

    @PutMapping("/markNotification/{id}")
    public ResponseEntity<NotificationDTO> markNotificationIsSeen(@PathVariable Long id) {
        return ResponseEntity.ok(notificationService.markNotificationIsSeen(id));
    }

    /**
     * Retrieves a list of notifications for a specific user.
     *
     * @param userId the ID of the user
     * @return a List of NotificationDTO objects representing the notifications for the user
     * @throws ResourceNotFoundException if no user entity is found for the given ID
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationDTO>> getNotificationsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.getNotificationsByUser(userId));
    }


    @GetMapping("/user/{userId}/top3")
    public ResponseEntity<List<NotificationDTO>> getTop3NotificationsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.getTop3NotificationsByUser(userId));
    }

    /**
     * Creates a new notification.
     *
     * @param notificationDto the {@link NotificationDTO} object representing the notification
     * @return the newly created {@link NotificationDTO} object
     */
    @PostMapping("/create")
    public ResponseEntity<NotificationDTO> createNotification(@RequestBody NotificationDTO notificationDto) {
        return new ResponseEntity<>(notificationService.createNotification(notificationDto), HttpStatus.CREATED);
    }

    /**
     * Updates an existing notification with the specified id.
     *
     * @param id              the id of the notification to update
     * @param notificationDto the updated notification information
     * @return the updated NotificationDTO object
     * @throws ResourceNotFoundException if the notification with the specified id is not found
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<NotificationDTO> updateNotification(@PathVariable Long id, @RequestBody NotificationDTO notificationDto) {
        return ResponseEntity.ok(notificationService.updateNotification(id, notificationDto));
    }

    /**
     * Deletes a notification with the specified ID.
     *
     * @param id the ID of the notification to delete
     * @return a ResponseEntity with a success status of HttpStatus.NO_CONTENT
     * @throws ResourceNotFoundException if the notification is not found with the given ID
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        notificationService.markAsDeleted(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}