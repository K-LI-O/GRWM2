package GRWM.backend.dto.tracker;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecurrenceConfig {
    @Builder.Default
    int  interval = 0; // 며칠마다 };
    @Builder.Default
    List<Integer> weekly = new ArrayList<>(); // 요일 (0=일요일, 6=토요일)};
    @Builder.Default
    int monthly = 0; // 몇 일에
}
