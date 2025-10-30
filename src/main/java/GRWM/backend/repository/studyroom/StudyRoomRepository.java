package GRWM.backend.repository.studyroom;

import GRWM.backend.entity.studyroom.StudyRoom;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudyRoomRepository extends JpaRepository<StudyRoom, Long> {
    List<StudyRoom> findByIsActiveTrue(Pageable pageable);
}
