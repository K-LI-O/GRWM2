package GRWM.backend.service;

import GRWM.backend.dto.personalPlanner.MemberCreateRequestDto;
import GRWM.backend.entity.Member;
import GRWM.backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    public boolean findDuplicateLoginId(String login_id){
        return memberRepository.existsByLoginId(login_id);
    }



    public Long createMember(MemberCreateRequestDto dto){

        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        Member member = new Member(dto.getUsername(), dto.getLoginId(), dto.getPassword(), dto.getEmail());

        // 패스워드 암호화

        Member savedMember = memberRepository.save(member);
        return savedMember.getId();
    }

    /*
    함수명 : findUsernameByLoginId
    기능 : 로그인 아이디로 사용자 이름 반환
    파라미터 : String loginId
    반환값 : String username
     */

    public String findUsernameByLoginId(String loginId){

        Optional<String> optionalUsername = memberRepository.findUsernameByLoginId(loginId);

        String username = null;
        try{
            if(optionalUsername.isPresent()) username = optionalUsername.get();
        } catch (Exception e) {
            throw new RuntimeException("해당 사용자가 존재하지 않습니다.");
        }
        return username;
    }

}
