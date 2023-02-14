package com.example.diplom.mapper;

import com.example.diplom.domain.UserNotification;
import com.example.diplom.dto.UserNotificationDTO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserNotificationMapper {
    UserNotificationMapper MAPPER = Mappers.getMapper(UserNotificationMapper.class);

    UserNotification toUserNotification(UserNotificationDTO dto);

    @InheritInverseConfiguration
    UserNotificationDTO fromUserNotification(UserNotification userNotification);

    List<UserNotification> toUserNotificationList(List<UserNotificationDTO> userNotificationDTOList);
    @InheritInverseConfiguration
    List<UserNotificationDTO> fromUserNotificationList(List<UserNotification> userNotifications);
}
