package GRWM.backend.repository.tracker;

import GRWM.backend.entity.tracker.TrackerTodo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrackerTodoRepository extends JpaRepository<TrackerTodo, Long> {
    Page<TrackerTodo> findByCreatorIdAndIsRecurring(Long creatorId, boolean isRecurring, Pageable pageable);

    List<TrackerTodo> findByCreatorIdAndIsRecurringTrueAndIsActive(Long creatorId, boolean status);

    List<TrackerTodo> findByIsRecurringTrueAndIsActiveTrue();
}
