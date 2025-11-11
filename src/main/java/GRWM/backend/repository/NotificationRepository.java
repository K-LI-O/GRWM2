package GRWM.backend.repository;

import GRWM.backend.entity.notification.Notification;
import GRWM.backend.entity.notification.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByIsSentFalseAndScheduledTimeBefore(Timestamp timestamp);
    Optional<Notification> findByReceiverIdAndMessageId(Long receiverId, Long messageId);
}
