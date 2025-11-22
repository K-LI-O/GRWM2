package GRWM.backend.entity.teamplanner;

import GRWM.backend.entity.user.Member;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;


@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamMember {
    /*
    {
Long Id,
Member member,
TeamPlanner planner,
String role; 멤버 등급(Manager, Member),
String nickname,
String status, 활동 중인 사용자와 탈퇴한 사용자 표시
LocalDateTime createdAt,
}

     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_member_id")
    @Setter(AccessLevel.NONE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_planner")
    private TeamPlanner teamPlanner;

    private String role; // manager, member
    private String nickname;
    private String status; // 살아있을 때는 active, 살아있지 않을 때는 withdraw


    @CreatedDate
    private LocalDateTime createdAt;



    public TeamMember(Member member, TeamPlanner teamPlanner, String role){
        this.member = member;
        this.teamPlanner = teamPlanner;
        this.role = role;
        this.nickname = "";
        this.status = "active";
    }

}
