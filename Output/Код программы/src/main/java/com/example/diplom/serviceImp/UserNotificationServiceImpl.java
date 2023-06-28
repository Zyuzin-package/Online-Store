package com.example.diplom.serviceImp;

import com.example.diplom.dao.UserNotificationRepository;
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
    public List<UserNotificationDTO> getNotificationsByUserName(String name) {
        List<UserNotification> list = userNotificationRepository.findNotificationByUserName(name);
        List<UserNotificationDTO> dtos = new ArrayList<>();
        for (UserNotification u : list) {
            dtos.add(new UserNotificationDTO(u));
        }

        return dtos;
    }

    @Override
    public UserNotificationDTO getNotificationsById(Long id) {
        UserNotification userNotification = userNotificationRepository.findFirstById(id);
        if(userNotification==null){
            return null;
        }
        return new UserNotificationDTO(userNotification);
    }

    @Override
    public void deleteNotificationByUserId(Long id) {
        userNotificationRepository.deleteUserNotificationById(id);
    }

    @Override
    public void sendNotificationToUser(UserNotificationDTO userNotificationDTO){
        UserNotification userNotification = UserNotification.builder()
                .message(userNotificationDTO.getMessage())
                .url(userNotificationDTO.getUrl())
                .urlText(userNotificationDTO.getUrlText())
                .userM(userService.findById(userNotificationDTO.getUserId()))
                .build();
        userNotificationRepository.save(userNotification);
    }

}
