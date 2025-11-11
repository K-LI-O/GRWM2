package GRWM.backend.entity.tracker;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrackerTodo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tracker_todo_id")
    @Setter(AccessLevel.NONE)
    private Long id;
    private Long creatorId;
    private String title;
    private String description;
    private LocalDate date;
    private boolean isCompleted;
    private boolean isPostponed;

    private boolean isRecurring;

    private String repeatRange;
    int repeatInterval; // 며칠마다 };
    @Builder.Default
    List<Integer> weekly = new ArrayList<>(); // 요일 (0=일요일, 6=토요일)};
    int monthly; // 몇 일에
    private boolean isActive;
    @Builder.Default
    List<Long> generatedTodos = new ArrayList<>();

}
