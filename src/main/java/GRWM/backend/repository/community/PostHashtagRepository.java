package GRWM.backend.repository.community;

import GRWM.backend.entity.community.Hashtag;
import GRWM.backend.entity.community.Post;
import GRWM.backend.entity.community.PostHashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PostHashtagRepository extends JpaRepository<PostHashtag, Long> {
    PostHashtag findByPostAndHashtag(Post post, Hashtag hashtag);

    List<PostHashtag> findAllByHashtagIn(List<Hashtag> hashtagList);

    List<PostHashtag> findByHashtag(Hashtag hashtag);

    List<PostHashtag> findByPost(Post post);
}
