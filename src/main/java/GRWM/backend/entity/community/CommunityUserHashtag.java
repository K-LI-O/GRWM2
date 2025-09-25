package GRWM.backend.entity.community;


import GRWM.backend.entity.user.CommunityUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@Entity
@RequiredArgsConstructor
public class CommunityUserHashtag {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private CommunityUser user;

    @ManyToOne
    @JoinColumn(name = "hashtag_id")
    private Hashtag hashtag;


    private List<String> hashtagOrder;
}
