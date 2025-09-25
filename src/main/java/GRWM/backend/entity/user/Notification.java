package GRWM.backend.entity;

import GRWM.backend.entity.community.CommunityUser;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class Notification {
    @Id
    @GeneratedValue
    Long id;

    String title;

    String description;

    @CreatedDate
    LocalDateTime createdAt;

    Boolean isRead;


}
