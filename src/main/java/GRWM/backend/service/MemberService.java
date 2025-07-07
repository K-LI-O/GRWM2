package GRWM.backend.service;

import GRWM.backend.dto.personalPlanner.MemberCreateRequestDto;
import GRWM.backend.entity.Member;
import GRWM.backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

}
