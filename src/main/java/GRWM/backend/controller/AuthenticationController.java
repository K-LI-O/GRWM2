package GRWM.backend.controller;


import GRWM.backend.dto.auth.FcmTokenRefreshDto;
import GRWM.backend.dto.auth.LoginRequestDto;
import GRWM.backend.dto.auth.LoginTokenResponse;
import GRWM.backend.dto.personalPlanner.MemberCreateRequestDto;
import GRWM.backend.jwt.JwtTokenProvider;
import GRWM.backend.repository.user.MemberRepository;
import GRWM.backend.service.MemberService;
import GRWM.backend.service.PushTokenService;
import GRWM.backend.service.community.CommunityUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {


    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;
    private final CommunityUserService communityUserService;
    private final PushTokenService pushTokenService;


        /*
    함수명 : createMember
    기능 : 멤버 정보를 받아 저장하고, 생성 후 회원 아이디를 반환;
    매개변수 : String username, String loginId, String password, String email
    반환값 : ResponseEntity<Long>; 200 ok와 사용자 ID(DB 테이블 Id, 로그인 아이디 아님)를 반환한다

     */

    @PostMapping("/signup")
    public ResponseEntity<Long> createMember(@RequestBody MemberCreateRequestDto dto) {
        Long savedMemberId = memberService.createMember(dto);
        return ResponseEntity.ok(savedMemberId);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginTokenResponse> login(@RequestBody LoginRequestDto dto){

        // 1. 인증 객체 생성: 사용자가 입력한 아이디와 비밀번호를 담은 Authentication 객체 (아직 인증되지 않음)
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(dto.getLoginId(), dto.getPassword());

        // 2. 인증 시도: AuthenticationManager를 통해 실제 인증 수행
        // UserDetailsService와 PasswordEncoder가 여기서 활용됨
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        // 3. SecurityContextHolder에 인증 정보 저장 (선택 사항이지만 일반적)
        // 이후 요청에서 @AuthenticationPrincipal 등으로 인증된 사용자 정보를 사용할 수 있게 됨
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // fcm 토큰 저장
        if(dto.getFcmToken() != null){
            pushTokenService.saveToken(
                    memberService.findUserIdByLoginId(dto.getLoginId()),
                    dto.getFcmToken()
            );

        }

        // 4. JWT 토큰 생성
        String jwt = jwtTokenProvider.generateToken(authentication);
        Long userId = memberService.findUserIdByLoginId(dto.getLoginId());


        // 5. 클라이언트에게 토큰 반환
        return ResponseEntity.ok(new LoginTokenResponse(
                jwt,
                "Bearer",
                memberService.findUsernameByLoginId(dto.getLoginId()),
                userId,
                communityUserService.findNicknameById(userId)
                ));

    }


    /*
    함수명 : isDuplicateValidation
    기능 : 200 ok와 함께 isDuplicate 의 bool 값을 전달한다. 반환값이 true 라면 같은 로그인 아이디의 회원이 존재한다.
    매개변수 : String userId(로그인 아이디입니다)
    반환값 : ResponseEntity<Boolean>

     */
    @GetMapping("/check-id/{userId}")
    public ResponseEntity<Boolean> idDuplicateValidation(@PathVariable String userId){
        boolean isDuplicate = memberService.findDuplicateLoginId(userId);
        if(isDuplicate){
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.ok(false);
    }

    /*
    name : refreshFcmToken
    function : 토큰 재발급 로직
    url : POST /api/auth/token-refresh
    Request : Dto - String fcmToken; String loginId;
    Response void;

     */
    @PostMapping("/token-refresh")
    public void refreshFcmToken(@RequestBody FcmTokenRefreshDto dto){
        if(dto.getFcmToken() != null){
            pushTokenService.saveToken(
                    dto.getUserId(),
                    dto.getFcmToken()
            );
        }
    }



}
