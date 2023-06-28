package com.example.diplom.service;

import com.example.diplom.dto.UserNotificationDTO;

import java.util.List;

public interface UserNotificationService {
    List<UserNotificationDTO> getAll();
    List<UserNotificationDTO> getNotificationsByUserName(String name);
    UserNotificationDTO getNotificationsById(Long id);

    void deleteNotificationByUserId(Long id);

    void sendNotificationToUser(UserNotificationDTO userNotificationDTO);

}
