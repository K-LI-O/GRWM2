package GRWM.backend.repository.tracker;

import GRWM.backend.entity.tracker.TrackerTodo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrackerTodoRepository extends JpaRepository<TrackerTodo, Long> {
    Page<TrackerTodo> findByCreatorId(Long creatorId, Pageable pageable);

    List<TrackerTodo> findByCreatorIdAndIsRecurringTrueAndRepeatRangeAndIsActive(Long creatorId, String type, boolean status);

    List<TrackerTodo> findByIsRecurringTrueAndIsActiveTrue();
}
