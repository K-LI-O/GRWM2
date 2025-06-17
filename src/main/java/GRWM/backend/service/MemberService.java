package GRWM.backend.service;

import GRWM.backend.dto.personalPlanner.MemberCreateRequestDto;
import GRWM.backend.entity.Member;
import GRWM.backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;


    public boolean findDuplicateLoginId(String login_id){
        return memberRepository.existsByLoginId(login_id);
    }



    public Long createMember(MemberCreateRequestDto dto){
        Member member = new Member(dto.getUsername(), dto.getLoginId(), dto.getPassword(), dto.getEmail());

        Member savedMember = memberRepository.save(member);

        return savedMember.getId();
    }

}
