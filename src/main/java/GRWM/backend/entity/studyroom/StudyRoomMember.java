package GRWM.backend.entity.studyroom;

import GRWM.backend.entity.user.CommunityUser;
import GRWM.backend.entity.user.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudyRoomMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_room_member_id")
    @Setter(AccessLevel.NONE)
    Long id;

    @ManyToOne
    CommunityUser user;

    @ManyToOne
    StudyRoom studyRoom;
}
