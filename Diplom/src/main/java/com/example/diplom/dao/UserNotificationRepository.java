package com.example.diplom.dao;

import com.example.diplom.domain.UserNotification;
import com.example.diplom.dto.UserNotificationDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserNotificationRepository extends JpaRepository<UserNotification, Long> {

    @Modifying
    @Query(value = "Select * from user_notification where user_notification.user_id=:id", nativeQuery = true)
    @Transactional
    List<UserNotification> findNotificationByUserId(@Param("id") Long userId);
    @Modifying
    @Query(value = "DELETE from user_notification where id=:id", nativeQuery = true)
    @Transactional
    void deleteUserNotificationById(@Param("id")Long id);
}
