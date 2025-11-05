package GRWM.backend.repository.tracker;

import GRWM.backend.entity.tracker.TomorrowMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TomorrowMessageRepository extends JpaRepository<TomorrowMessage, Long> {
}
