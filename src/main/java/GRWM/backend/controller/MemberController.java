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




}
