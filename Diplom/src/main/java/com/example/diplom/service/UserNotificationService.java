package com.example.diplom.service;


import com.example.models.domain.statistics.*;
import com.example.models.dto.statistics.*;
import com.example.models.dto.*;
import com.example.models.domain.*;



import java.util.List;

public interface UserNotificationService {
    List<UserNotificationDTO> getAll();
    List<UserNotificationDTO> getNotificationsByUserName(String name);
    UserNotificationDTO getNotificationsById(Long id);

    void deleteNotificationByUserId(Long id);

    void sendNotificationToUser(UserNotificationDTO userNotificationDTO);

}
