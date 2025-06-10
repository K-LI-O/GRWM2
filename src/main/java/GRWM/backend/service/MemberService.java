package GRWM.backend.service;

import GRWM.backend.dto.MemberCreateRequestDto;
import GRWM.backend.entity.Member;
import GRWM.backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;


    public boolean findDuplicateLoginId(String login_id){
        return memberRepository.existsByLoginId(login_id);
    }



    public void createMember(MemberCreateRequestDto dto){
        Member member = new Member(dto.getUsername(), dto.getLoginId(), dto.getPassword(), dto.getEmail());

        memberRepository.save(member);
    }

}
