package GRWM.backend.entity.tracker;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

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

    private Range repeatRange;
    private boolean isActive;
    private int activeCount;
    private int totalCount;

}
