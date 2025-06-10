package GRWM.backend.controller;

import GRWM.backend.dto.MemberCreateRequestDto;
import GRWM.backend.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/create")
    public void createMember(MemberCreateRequestDto dto) {
        memberService.createMember(dto);
    }

}
