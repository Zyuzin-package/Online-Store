package com.example.diplom.mapper;


import com.example.models.domain.statistics.*;
import com.example.models.dto.statistics.*;
import com.example.models.dto.*;
import com.example.models.domain.*;


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
