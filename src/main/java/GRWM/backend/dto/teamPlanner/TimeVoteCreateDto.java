package GRWM.backend.dto.teamPlanner;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TimeVoteCreateDto {
    /*
String title,
List<LocalDate> voteRange, (투표 범위 5일(떨어진 날짜 가능))
LocalDateTime finishTime, (마감 기한}
List<Long> memberIds (투표에 참여하는 사람들의 id 목록)
     */

    private String title;
    private List<LocalDate> voteRange;
    private LocalDateTime finishTime;
    private List<Long> MemberIds;
    private LocalTime startHour;
    private LocalTime endHour;
}
