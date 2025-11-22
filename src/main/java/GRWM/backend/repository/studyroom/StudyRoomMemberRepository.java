package GRWM.backend.repository.studyroom;

import GRWM.backend.entity.studyroom.StudyRoomMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudyRoomMemberRepository extends JpaRepository<StudyRoomMember, Long> {
    List<StudyRoomMember> findByUser_Id(Long userId);
}
