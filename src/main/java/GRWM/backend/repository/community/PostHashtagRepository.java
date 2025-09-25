package GRWM.backend.repository.community;

import GRWM.backend.entity.community.PostHashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PostHashtagRepository extends JpaRepository<PostHashtag, Long> {
}
