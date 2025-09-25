package GRWM.backend.entity.community;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
public class BlockList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blocklist_id")
    @Setter(AccessLevel.NONE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "blocking_user")
    private CommunityUser blocker;

    @ManyToOne
    @JoinColumn(name = "blocked_user")
    private CommunityUser blockedUser;
}
