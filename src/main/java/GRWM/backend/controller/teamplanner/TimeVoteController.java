package GRWM.backend.controller.teamplanner;

import GRWM.backend.service.teamplanner.TimeVoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TimeVoteController {

    private final TimeVoteService timeVoteService;
}
