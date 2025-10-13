package GRWM.backend.repository.community;

import GRWM.backend.entity.community.Following;
import GRWM.backend.entity.user.CommunityUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowingRepository extends JpaRepository<Following, Long> {

    Following findByFollowingAndFollower(CommunityUser following, CommunityUser follower);

    boolean existsByFollowingAndFollower(CommunityUser following, CommunityUser follower);

    List<Following> findByFollowing(CommunityUser following);

    List<Following> findByFollower(CommunityUser follower);
}
