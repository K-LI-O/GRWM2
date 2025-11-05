package GRWM.backend.repository;

import GRWM.backend.entity.notification.PushToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PushTokenRepository extends JpaRepository<PushToken, Long> {
PushToken findByMember_Id(Long memberId);
}
