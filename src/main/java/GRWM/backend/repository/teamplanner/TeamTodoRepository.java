package GRWM.backend.repository.teamplanner;

import GRWM.backend.entity.teamplanner.TeamTodo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamTodoRepository extends JpaRepository<TeamTodo, Long> {
}
