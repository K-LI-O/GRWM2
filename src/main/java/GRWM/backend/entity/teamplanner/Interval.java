package GRWM.backend.entity.teamplanner;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
@Setter
@Getter
@AllArgsConstructor
@Builder
public class Interval {

    private LocalTime startTime;
    private LocalTime endTime;

}
