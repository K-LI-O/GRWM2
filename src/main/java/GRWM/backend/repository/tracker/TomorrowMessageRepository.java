package GRWM.backend.repository.tracker;

import GRWM.backend.entity.tracker.TomorrowMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TomorrowMessageRepository extends JpaRepository<TomorrowMessage, Long> {

    Optional<TomorrowMessage> findByCreator_IdAndScheduledTimeAfter(Long userId, LocalDateTime now);
}
