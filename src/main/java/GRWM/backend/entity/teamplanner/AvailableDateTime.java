package GRWM.backend.entity.teamplanner;

import jakarta.persistence.Embeddable;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AvailableDateTime {

    private LocalDate date;

    private List<Interval> intervals;
}

