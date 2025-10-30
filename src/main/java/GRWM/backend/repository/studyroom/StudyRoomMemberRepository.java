package GRWM.backend.repository.studyroom;

import GRWM.backend.entity.studyroom.StudyRoomMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyRoomMemberRepository extends JpaRepository<StudyRoomMember, Long> {

}
