package GRWM.backend.service;

import GRWM.backend.dto.personalPlanner.MemberCreateRequestDto;
import GRWM.backend.entity.user.Member;
import GRWM.backend.entity.user.CommunityUser;
import GRWM.backend.repository.user.CommunityUserRepository;
import GRWM.backend.repository.user.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final CommunityUserRepository cuRepository;
    private final PasswordEncoder passwordEncoder;


    public boolean findDuplicateLoginId(String login_id){
        return memberRepository.existsByLoginId(login_id);
    }



    public Long createMember(MemberCreateRequestDto dto){

        dto.setPassword(passwordEncoder.encode(dto.getPassword()));

        Member member = new Member(dto.getUsername(), dto.getLoginId(), dto.getPassword(), dto.getEmail());
        Member savedMember = memberRepository.save(member);


        createCommunityUser(savedMember);
        return savedMember.getId();
    }

    private void createCommunityUser(Member member){
        CommunityUser cu = new CommunityUser();
        cu.setNickname("무명"+member.getId());
        cuRepository.save(cu);

        member.setCommunityUser(cu);
        memberRepository.save(member);
    }


    /*
    함수명 : findUsernameByLoginId
    기능 : 로그인 아이디로 사용자 이름 반환
    파라미터 : String loginId
    반환값 : String username
     */

    public String findUsernameByLoginId(String loginId){

        return memberRepository.findUsernameByLoginId(loginId)
                .orElseThrow(() -> new RuntimeException("해당 사용자가 존재하지 않습니다."));
    }

    /*
    함수명 : findUsernameByLoginId
    기능 : 로그인 아이디로 사용자 이름 반환
    파라미터 : String loginId
    반환값 : String username
     */

    public Long findUserIdByLoginId(String loginId){

        return memberRepository.findIdByLoginId(loginId)
                .orElseThrow(() -> new RuntimeException("해당 사용자가 존재하지 않습니다."));
    }


}
