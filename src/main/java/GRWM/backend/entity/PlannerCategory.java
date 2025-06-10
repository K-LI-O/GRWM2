package GRWM.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class PlannerCategory {
    @Id
    @Column(name = "categoryId")
    private Long id;

    @Column(name = "categoryName")
    private String name;

    @Column
    private String color;

    // 카테고리와 플래너 (다대일)
    @ManyToOne(fetch = FetchType.LAZY)
    private PersonalPlanner personalPlanner;


}
