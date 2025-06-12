package GRWM.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SCHEDULE_ID)")
    private Long id;

    @Column
    private String title;

    @Column
    private LocalDateTime startDateTime;
    @Column
    private LocalDateTime finishDateTime;
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
    @JoinColumn(nullable = true)
    private PlannerCategory plannerCategory;


    // 생성자
    public Schedule(String title, LocalDateTime startDateTime, LocalDateTime finishDateTime, String location, String memo, PersonalPlanner pp, PlannerCategory pc){

        this.title = title;
        this.startDateTime = startDateTime;
        this.finishDateTime = finishDateTime;
        this.location = location;
        this.memo = memo;
        this.personalPlanner = pp;
        this.plannerCategory = pc;
    }

}
