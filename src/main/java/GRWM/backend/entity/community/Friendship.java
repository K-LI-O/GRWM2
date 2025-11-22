package GRWM.backend.entity.community;

import GRWM.backend.entity.user.CommunityUser;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Friendship {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne
    CommunityUser user1;
    @ManyToOne
    CommunityUser user2;

    @CreatedDate
    private LocalDateTime createdAt;
}
