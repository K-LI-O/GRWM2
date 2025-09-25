package GRWM.backend.repository.community;

import GRWM.backend.entity.community.CommunityUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityUserRepository extends JpaRepository<CommunityUser, Long> {

}
