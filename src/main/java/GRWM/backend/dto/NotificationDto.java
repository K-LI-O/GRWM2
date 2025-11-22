package GRWM.backend.dto;

import GRWM.backend.entity.notification.NotificationType;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class NotificationDto {

    Long id;
    NotificationType type;
    String title;
    String body;
    LocalDateTime createdAt;
    boolean isRead;
}
