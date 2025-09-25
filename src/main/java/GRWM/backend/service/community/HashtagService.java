package GRWM.backend.service.community;

import GRWM.backend.repository.community.CommunityUserHashtagRepository;
import GRWM.backend.repository.community.HashtagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HashtagService {

    private final HashtagRepository hashtagRepository;
    private final CommunityUserHashtagRepository cuHashtagRepository;
}
