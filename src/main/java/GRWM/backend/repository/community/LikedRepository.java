package GRWM.backend.repository.community;

import GRWM.backend.entity.community.Liked;
import GRWM.backend.entity.community.Post;
import GRWM.backend.entity.user.CommunityUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikedRepository extends JpaRepository<Liked, Long> {
    Liked findByPostAndUser(Post post, CommunityUser user);
}
