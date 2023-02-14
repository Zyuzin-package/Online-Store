package com.example.diplom.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data//Геттер сеттер tostring equals
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user_notification", schema = "public")
public class UserNotification {
    private static final String SEQ_NAME = "user_notification_seq";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_NAME)
    @SequenceGenerator(name = SEQ_NAME, sequenceName = SEQ_NAME, allocationSize = 1)
    private Long id;

    @Enumerated(EnumType.ORDINAL) // В базе хранится номер
    private Notification notification;

    @ManyToOne
    @JoinColumn(name = "user_id")
    UserM userM;

}
