package GRWM.backend.dto.tracker;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class DiaryCreateDto {
    String title;
    String content;
    String category;
    String emotion;
    List<String> tags;
    LocalDate date;

}
