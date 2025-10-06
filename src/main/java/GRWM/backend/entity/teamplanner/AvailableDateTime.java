package GRWM.backend.entity.teamplanner;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Setter
@Getter
@Embeddable
public class AvailableDateTime {

    private LocalDate date;
    private Interval intervals;
}
@Setter
@Getter
@Embeddable
class Interval{
    private LocalTime startTime;
    private LocalTime endTime;
}
