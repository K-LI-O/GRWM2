package GRWM.backend.controller;

import GRWM.backend.dto.personalPlanner.MemberCreateRequestDto;
import GRWM.backend.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user")
public class MemberController {

    private final MemberService memberService;


    /*
    함수명 : isDuplicateValidation
    기능 : 200 ok와 함께 isDuplicate 의 bool 값을 전달한다. 반환값이 true 라면 같은 로그인 아이디의 회원이 존재한다.
    매개변수 : String userId(로그인 아이디입니다)
    반환값 : ResponseEntity<Boolean>

     */
    @GetMapping("/create/check-id/{userId}")
    public ResponseEntity<Boolean> idDuplicateValidation(@PathVariable String userId){
        boolean isDuplicate = memberService.findDuplicateLoginId(userId);
        if(isDuplicate){
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.ok(false);
    }


    /*
    함수명 : createMember
    기능 : 멤버 정보를 받아 저장하고, 생성 후 회원 아이디를 반환;
    매개변수 : String username, String loginId, String password, String email
    반환값 : ResponseEntity<Long>; 200 ok와 사용자 ID(DB 테이블 Id, 로그인 아이디 아님)를 반환한다

     */
    @PostMapping("/create")
    public ResponseEntity<Long> createMember(@RequestBody MemberCreateRequestDto dto) {
        Long savedMemberId = memberService.createMember(dto);
        return ResponseEntity.ok(savedMemberId);
    }



}
