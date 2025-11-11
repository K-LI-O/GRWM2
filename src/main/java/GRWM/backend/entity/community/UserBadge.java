package GRWM.backend.entity.community;

import GRWM.backend.entity.user.CommunityUser;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@NoArgsConstructor
public class UserBadge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_badge_id")
    @Setter(AccessLevel.NONE)
    private Long id;


    @ManyToOne
    @JoinColumn
    private Badge badge;

    @ManyToOne
    @JoinColumn
    private CommunityUser user;
}
