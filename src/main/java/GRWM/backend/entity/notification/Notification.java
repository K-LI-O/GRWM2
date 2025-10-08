package GRWM.backend.entity.notification;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Entity
@EntityListeners(EnableJpaAuditing.class)
public class Notification {

    @Id
    @GeneratedValue
    private Long id;
    private Long receiverId;
    private Long senderId;
    private NotificationType type; // comment, like, follow, forMeTomorrow, Schedule
    private String content;
    private boolean isRead;
    @CreatedDate
    private LocalDateTime createdAt;

}
