package com.example.diplom.serviceImp;

import com.example.diplom.dao.UserNotificationRepository;
import com.example.diplom.domain.Notification;
import com.example.diplom.domain.UserNotification;
import com.example.diplom.dto.UserNotificationDTO;
import com.example.diplom.mapper.UserNotificationMapper;
import com.example.diplom.service.UserNotificationService;
import com.example.diplom.service.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserNotificationServiceImpl implements UserNotificationService {

    private final UserNotificationMapper mapper = UserNotificationMapper.MAPPER;
    private final UserNotificationRepository userNotificationRepository;
    private final UserService userService;

    public UserNotificationServiceImpl(UserNotificationRepository userNotificationRepository, UserService userService) {
        this.userNotificationRepository = userNotificationRepository;
        this.userService = userService;
    }

    @Override
    public List<UserNotificationDTO> getAll() {
        return mapper.fromUserNotificationList(userNotificationRepository.findAll());
    }

    @Override
    public List<UserNotificationDTO> getNotificationsByUserId(Long id) {
        List<UserNotification> list = userNotificationRepository.findNotificationByUserId(id);
        List<UserNotificationDTO> dtos = new ArrayList<>();
        for (UserNotification u : list) {
            dtos.add(new UserNotificationDTO(u));
        }

        return dtos;
    }

    @Override
    public UserNotificationDTO getNotificationsById(Long id) {
        UserNotification userNotification =  userNotificationRepository.findFirstById(id);
        return new UserNotificationDTO(userNotification);
    }

    @Override
    public void deleteNotificationByUserId(Long id) {
        userNotificationRepository.deleteUserNotificationById(id);
    }

    @Override
    public void sendNotificationToUser(Long id, Notification notification) {
        UserNotification userNotification = UserNotification.builder()
                .notification(notification)
                .userM(userService.findById(id))
                .build();
        userNotificationRepository.save(userNotification);
    }

    @Override
    public void updateNotification(Long notificationId, Notification notification, Long userId){
        UserNotification userNotification = UserNotification.builder()
                .id(notificationId)
                .notification(notification)
                .userM(userService.findById(userId))
                .build();
        userNotificationRepository.save(userNotification);
    }
}
