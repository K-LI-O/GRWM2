package GRWM.backend.entity.community;

import GRWM.backend.entity.user.CommunityUser;
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

    @ManyToOne
    @JoinColumn(name = "following")
    private CommunityUser following;

    @ManyToOne
    @JoinColumn(name = "follower")
    private CommunityUser follower;


    public Following(CommunityUser following, CommunityUser follower){
        this.following = following;
        this.follower = follower;
    }
}




