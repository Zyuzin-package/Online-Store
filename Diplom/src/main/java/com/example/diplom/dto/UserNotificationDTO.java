package com.example.diplom.dto;

import com.example.diplom.domain.UserNotification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserNotificationDTO {
    private Long id;
    private String message;
    private String url;
    private String urlText;
    private Long userId;

    public UserNotificationDTO(UserNotification userNotification) {
        this.id =userNotification.getId();
        this.message = userNotification.getMessage();
        this.url = userNotification.getUrl();
        this.userId = userNotification.getUserM().getId();
        this.urlText = userNotification.getUrlText();
    }
}
