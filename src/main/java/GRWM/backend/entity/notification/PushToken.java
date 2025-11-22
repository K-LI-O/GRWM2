package GRWM.backend.entity.notification;

import GRWM.backend.entity.user.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EntityListeners(EnableJpaAuditing.class)
public class PushToken {
    /*
PushToken (DeviceToken) 테이블
id (PK)
user_id (FK to User 테이블)
fcm_token (String, 토큰 값)
device_type (String, 예: 'Android', 'iOS')
created_at (Timestamp)
updated_at (Timestamp, 토큰 갱신 시점)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private Member member;

    private String fcmToken;

    @Builder.Default
    private String deviceType = "web"; // WEB

    @CreatedDate
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;


}
