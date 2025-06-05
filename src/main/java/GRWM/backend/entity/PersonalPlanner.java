package GRWM.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class PersonalPlanner {

    @Id
    @Column(name = "PERSONAL_PLANNER_ID")
    private String id;

    @Column
    private String name;

    @Column
    private String profileImage;

    @Column
    private String explanation;

    // 사용자와의 단방향 다대일 관계
    @ManyToOne(fetch = FetchType.LAZY)
    private Member creator;

    // 플래너의 스케줄(일대다)
    // 단방향 매핑 처리로 삭제

    // 플래너의 카테고리(일대다)
    // 단방향 매핑 처리로 삭제
}
