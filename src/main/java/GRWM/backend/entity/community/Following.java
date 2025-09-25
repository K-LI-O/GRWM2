package GRWM.backend.entity.community;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
public class Following {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "following_id")
    @Setter(AccessLevel.NONE)
    private Long id;

    @OneToOne
    private CommunityUser following;

    @OneToOne
    private CommunityUser follower;
}
