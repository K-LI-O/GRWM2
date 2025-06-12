package GRWM.backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class PersonalPlanner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PERSONAL_PLANNER_ID")
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column
    private String name;

    @Column
    private String profileImage;

    @Column
    private String explanation;

    // 사용자와의 단방향 다대일 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private Member creator;

    // 플래너의 스케줄(일대다)
    // 단방향 매핑 처리로 삭제

    // 플래너의 카테고리(일대다)
    // 단방향 매핑 처리로 삭제
    public PersonalPlanner(Member creator, String name, String explanation, String profileImage){
        this.name = name;
        this.explanation = explanation;
        this.profileImage = profileImage;
        this.creator = creator;
    }



}
