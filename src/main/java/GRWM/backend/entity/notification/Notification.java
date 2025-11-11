package GRWM.backend.entity.notification;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    private Long messageId; // 내일의 나에게 메시지 수정 경우;
    private boolean isRead;

    String title; // 	String	알림 제목 ("팔로우 알림")
    String body; //	String	알림 내용 (Content)
    Timestamp scheduledTime; //	알림 발송 예정 시간 (가장 중요)
    @Builder.Default
    boolean isSent = false; //	Boolean	발송 완료 여부 (false 로 초기화)

    @CreatedDate
    private LocalDateTime createdAt;

}
