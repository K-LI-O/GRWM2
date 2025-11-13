package GRWM.backend.repository.community;

import GRWM.backend.entity.community.Hashtag;
import GRWM.backend.entity.community.Post;
import GRWM.backend.entity.community.PostHashtag;
import org.hibernate.query.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PostHashtagRepository extends JpaRepository<PostHashtag, Long> {

    PostHashtag findByPostAndHashtag(Post post, Hashtag hashtag);

    List<PostHashtag> findAllByHashtagIn(List<Hashtag> hashtagList);

    Slice<PostHashtag> findByHashtag(Hashtag hashtag, Pageable pageable);

    List<PostHashtag> findByPost(Post post);




}
