package GRWM.backend.repository.community;

import GRWM.backend.entity.community.Following;
import GRWM.backend.entity.user.CommunityUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowingRepository extends JpaRepository<Following, Long> {

    public Following findByFollowingAndFollower(CommunityUser following, CommunityUser follower);

    public Following findByFollowing(CommunityUser following);
}
