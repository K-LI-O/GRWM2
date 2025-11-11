package GRWM.backend.repository.community;

import GRWM.backend.entity.community.BlockList;
import GRWM.backend.entity.community.Following;
import GRWM.backend.entity.user.CommunityUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlockListRepository extends JpaRepository<BlockList, Long> {

    BlockList findByBlockerAndBlockedUser(CommunityUser blocker, CommunityUser blocked_user);

    List<BlockList> findByBlocker(CommunityUser blocker);

    List<BlockList> findByBlockedUser(CommunityUser blockedUser);

}
