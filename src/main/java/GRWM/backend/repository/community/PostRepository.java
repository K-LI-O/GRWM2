package GRWM.backend.repository.community;

import GRWM.backend.entity.community.Post;
import GRWM.backend.entity.user.CommunityUser;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Slice<Post> findAllByUserIn(List<CommunityUser> followingList, Pageable pageable);



    Slice<Post> findByUser(CommunityUser user, Pageable pageable);

    List<Post> findByContentContaining(String keyword);

}
