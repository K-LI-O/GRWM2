package GRWM.backend.dto.tracker;

import GRWM.backend.entity.tracker.Diary;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiaryListDto {
    List<DiaryDto> diaries;
    Long totalCount;
    int currentPage;
    int totalPages;
}
