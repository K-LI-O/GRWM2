package GRWM.backend.entity.teamplanner;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.time.LocalTime;
@Setter
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Interval {

    private LocalTime startTime;
    private LocalTime endTime;

}
