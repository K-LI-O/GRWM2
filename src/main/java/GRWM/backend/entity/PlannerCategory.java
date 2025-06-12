package GRWM.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class PlannerCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "categoryId")
    private Long id;

    @Column(name = "categoryName")
    private String name;

    @Column
    private String color;

    // 카테고리와 플래너 (다대일)
    @ManyToOne(fetch = FetchType.LAZY)
    private PersonalPlanner personalPlanner;


    // 생성자
    public PlannerCategory(String name, String color, PersonalPlanner personalPlanner){
        this.name = name;
        this.color = color;
        this.personalPlanner = personalPlanner;
    }


}
