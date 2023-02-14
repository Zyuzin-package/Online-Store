package com.example.diplom.service;

import com.example.diplom.dto.UserNotificationDTO;

import java.util.List;

public interface UserNotificationService {
    List<UserNotificationDTO> getAll();
    List<UserNotificationDTO> getNotificationsByUserId(Long id);

    void deleteNotificationByUserId(Long id);
}
