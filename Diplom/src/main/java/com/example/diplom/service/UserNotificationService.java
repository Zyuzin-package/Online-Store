package com.example.diplom.service;

import com.example.diplom.domain.Notification;
import com.example.diplom.dto.UserNotificationDTO;

import java.util.List;

public interface UserNotificationService {
    List<UserNotificationDTO> getAll();
    List<UserNotificationDTO> getNotificationsByUserId(Long id);

    List<UserNotificationDTO> getNotificationsByUserName(String name);
    UserNotificationDTO getNotificationsById(Long id);

    void deleteNotificationByUserId(Long id);

    void sendNotificationToUser(Long id, Notification notification);

    void updateNotification(Long notificationId, Notification notification, Long userId);
}
