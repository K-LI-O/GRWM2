package GRWM.backend.repository.user;

import GRWM.backend.entity.user.CommunityUser;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityUserRepository extends JpaRepository<CommunityUser, Long> {
    Slice<CommunityUser> findByNicknameContaining(String keyword, Pageable pageable);
}
