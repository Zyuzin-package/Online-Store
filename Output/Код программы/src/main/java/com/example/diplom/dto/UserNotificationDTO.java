package com.example.diplom.dto;

import com.example.diplom.domain.UserNotification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlText() {
        return urlText;
    }

    public void setUrlText(String urlText) {
        this.urlText = urlText;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
