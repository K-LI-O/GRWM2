package GRWM.backend.controller;

import GRWM.backend.repository.user.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/users")
public class MemberController {


    private final MemberRepository memberRepository;

    /*
    함수명 : getUserInfo
    기능 : 사용자 프로필의 간단한 정보를 가져온다.
    매개변수 : userId
    반환값 : userId, username, loginId, email;
     */


    /*
    함수명 : getCommunityUserInfo
    기능 : 커뮤니티 프로필의 간단한 정보를 가져온다.
    매개변수 : userId
    반환값 : userId, username, loginId, email;
     */


}
