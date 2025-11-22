package GRWM.backend.repository.studyroom;

import GRWM.backend.entity.studyroom.StudyRoomTodo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyRoomTodoRepository extends JpaRepository<StudyRoomTodo, Long> {
}
