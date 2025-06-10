package GRWM.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class Schedule {

    @Id
    @Column(name = "SCHEDULE_ID)")
    private Long id;

    @Column
    private String title;

    @Column
    private LocalDateTime startTime;
    @Column
    private LocalDateTime finishTime;
    // LocalDateTime 은 시간대 처리가 없는 순수 날짜 및 시간이므로 해당 기능 처리가 더 필요함

    @Column
    private String location;

    @Column
    private String memo;

    // 추후 일정 반복 설정 추가

    // 플래너와의 단방향 다대일 관계
    @ManyToOne(fetch = FetchType.LAZY)
    private PersonalPlanner personalPlanner;

    // 카테고리와의 단방향 다대일 관계
    @ManyToOne(fetch = FetchType.LAZY)
    private PlannerCategory plannerCategory;


}
