package GRWM.backend.service.teamplanner;

import GRWM.backend.dto.teamPlanner.TeamTodoDto;
import GRWM.backend.dto.teamPlanner.TeamTodoUpdateDto;
import GRWM.backend.entity.teamplanner.TeamSchedule;
import GRWM.backend.entity.teamplanner.TeamTodo;
import GRWM.backend.entity.user.Member;
import GRWM.backend.repository.teamplanner.TeamScheduleRepository;
import GRWM.backend.repository.teamplanner.TeamTodoRepository;
import GRWM.backend.repository.user.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TeamTodoService {

    private final MemberRepository memberRepository;
    private final TeamTodoRepository teamTodoRepository;
    private final TeamScheduleRepository scheduleRepository;
    /*
    name : createTodoList
    function : 투두리스트 생성
    POST /api/team-planner/{plannerId}/schedule/{scheduleId}/todo
    param : Long plannerId, Long scheduleId, TeamTodoDto
    return value : Long todoId
    */

    public Long createTodoList(Long plannerId, Long scheduleId, TeamTodoDto dto){

        TeamTodo todo = TeamTodo.builder()
                .teamSchedule(extractOptionalSchedule(scheduleId))
                .creator(extractOptionalMember(dto.getUser().getUserId()))
                .content(dto.getContent())
                .isPrivate(dto.isPrivate())
                .build();

        TeamTodo savedTodo = teamTodoRepository.save(todo);
        return savedTodo.getId();
    }

    /*
    name : updateTodo
    function : 투두리스트 수정 및 완료 체크
    PUT/api/team-planner/{plannerId}/schedule/{scheduleId}/todo/{todoId}
    param : Long plannerId, Long scheduleId, Long todoId
    return value : TeamTodoUpdateDto
    */
    public TeamTodoUpdateDto updateTodo(Long plannerId, Long scheduleId, Long todoId, Long userId,
                                        TeamTodoUpdateDto dto){
        // todo 객체 불러오기
        Optional<TeamTodo> optionalTodo = teamTodoRepository.findById(todoId);
        if(optionalTodo.isEmpty()){
            throw new RuntimeException("존재하지 않는 투두리스트입니다.");
        }
        TeamTodo todo = optionalTodo.get();

        // 체크하려는 사람과 투두 작성자가 동일한지 확인
        if(!todo.getCreator().getId().equals(userId)){
            throw new RuntimeException("투두리스트 수정 권한이 없습니다.");
        }

        todo.setContent(dto.getContent());
        todo.setCompleted(dto.isComplete());
        todo.setPrivate(dto.isPrivate());

        teamTodoRepository.save(todo);

        return dto;
    }

    /*
    name : deleteTodo
    function : 투두리스트 삭제
    DELETE /api/team-planner/{plannerId}/schedule/{scheduleId}/todo/{todoId}
    Request: Long plannerId, Long scheduleId, Long todoId
    Response: x
     */

    public void deleteTodo(Long plannerId, Long scheduleId, Long todoId){
        teamTodoRepository.deleteById(todoId);
    }


    // ======== private logics ======= //
    private Member extractOptionalMember(Long userId) throws RuntimeException{
        Optional<Member> optionalMember = memberRepository.findById(userId);

        if(optionalMember.isPresent()){
            return optionalMember.get();
        }
        else{
            throw new RuntimeException("존재하지 않는 사용자입니다.");
        }
    }

    private TeamSchedule extractOptionalSchedule(Long scheduleId) {
        Optional<TeamSchedule> optionalSchedule = scheduleRepository.findById(scheduleId);

        if(optionalSchedule.isEmpty()){
            throw new RuntimeException("존재하지 않는 스케줄입니다.");
        }
        return optionalSchedule.get();
    }




}
