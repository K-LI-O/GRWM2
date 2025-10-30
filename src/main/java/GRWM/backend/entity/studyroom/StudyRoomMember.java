package GRWM.backend.entity.studyroom;

import GRWM.backend.entity.user.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class StudyRoomMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_room_member_id")
    @Setter(AccessLevel.NONE)
    Long id;

    @ManyToOne
    Member member;

    @ManyToOne
    StudyRoom studyRoom;
}
