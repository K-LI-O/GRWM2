package GRWM.backend.repository.community;


import GRWM.backend.entity.community.CommunityUserHashtag;
import GRWM.backend.entity.community.Hashtag;
import GRWM.backend.entity.user.CommunityUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommunityUserHashtagRepository extends JpaRepository<CommunityUserHashtag, Long> {

    List<CommunityUserHashtag> findByUser(CommunityUser user);

    CommunityUserHashtag findByUserAndHashtag(CommunityUser user, Hashtag hashtag);

}
