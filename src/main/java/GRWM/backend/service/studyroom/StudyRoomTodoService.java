package GRWM.backend.service.studyroom;

import GRWM.backend.dto.studyroom.ReactionDto;
import GRWM.backend.dto.studyroom.StudyRoomTodoCreateDto;
import GRWM.backend.dto.studyroom.StudyRoomTodoDto;
import GRWM.backend.entity.studyroom.Reaction;
import GRWM.backend.entity.studyroom.StudyRoom;
import GRWM.backend.entity.studyroom.StudyRoomTodo;
import GRWM.backend.entity.user.CommunityUser;
import GRWM.backend.repository.studyroom.ReactionRepository;
import GRWM.backend.repository.studyroom.StudyRoomRepository;
import GRWM.backend.repository.studyroom.StudyRoomTodoRepository;
import GRWM.backend.repository.user.CommunityUserRepository;
import jakarta.transaction.TransactionScoped;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class StudyRoomTodoService {

    private final StudyRoomRepository studyRoomRepository;
    private final StudyRoomTodoRepository studyRoomTodoRepository;
    private final CommunityUserRepository communityUserRepository;
    private final ReactionRepository reactionRepository;
    private final SimpMessagingTemplate messagingTemplate; // 메시지 전파 도구

    /*
    name : getTodoList
    URL: GET /api/study-rooms/{studyRoomId}/todos
    param: Long studyRoomId
    return value : List<StudyRoomTodoDto> todos
    */
    public List<StudyRoomTodoDto> getTodoList(Long studyRoomId){
        StudyRoom studyRoom = extractOptionalRoom(studyRoomId);
        return todoToDtoList(studyRoom.getTodoList());
    }

    /*
    name : createTodo
    URL: POST /api/study-rooms/{studyRoomId}/todos
    param : Long studyRoomId, CreateTodoDto, userDetails
    return value : StudyRoomTodoDto
    */
    public StudyRoomTodoDto createTodo(Long studyRoomId, StudyRoomTodoCreateDto dto, Long communityId){
        StudyRoom studyRoom = extractOptionalRoom(studyRoomId);
        // 투두 생성 및 저장
        StudyRoomTodo todo = StudyRoomTodo.builder()
                .studyRoom(studyRoom)
                .creator(extractOptionalUser(communityId))
                .title(dto.getTitle())
                .description(dto.getDescription())
                .build();
        StudyRoomTodo savedTodo = studyRoomTodoRepository.save(todo);

        // dto 생성
        StudyRoomTodoDto result = todoToDto(savedTodo);

        // 웹소켓으로 전파
        String destination = "/topic/studyroom."+ studyRoomId +".todo";
        messagingTemplate.convertAndSend(destination, result);
        return result;
    }


    /*
    name : updateTodo
    URL: PUT /api/study-rooms/{studyRoomId}/todos/{todoId}
    param : StudyRoomTodoCreateDto
    return value : StudyRoomTodoDto
    */
    public StudyRoomTodoDto updateTodo(Long studyRoomId, Long todoId, StudyRoomTodoCreateDto dto){
        StudyRoom studyRoom = extractOptionalRoom(studyRoomId);

        // 투두 가져오기
        StudyRoomTodo todo = extractOptionalTodo(todoId);
        todo.setTitle(dto.getTitle());
        todo.setDescription(dto.getDescription());
        StudyRoomTodo savedTodo = studyRoomTodoRepository.save(todo);

        // dto 생성
        StudyRoomTodoDto result = todoToDto(savedTodo);
        // 웹소켓으로 전파
        String destination = "/topic/studyroom."+ studyRoomId +".todo";
        messagingTemplate.convertAndSend(destination, result);
        return result;
    }

    /*
    name : deleteTodo
    URL: DELETE /api/study-rooms/{studyRoomId}/todos/{todoId}
    param : Long studyRoomId, Long todoId
    return value : ResponseEntity 204
    */
    public void deleteTodo(Long studyRoomId, Long todoId){
        StudyRoom studyRoom = extractOptionalRoom(studyRoomId);
        StudyRoomTodo todo = extractOptionalTodo(todoId);

        // 투두 삭제
        studyRoomTodoRepository.delete(todo);
    }

    /*
    name : completeTodo
    URL: PATCH /api/study-rooms/{studyRoomId}/todos/{todoId}/complete
    param : Long studyRoomId, Long todoId
    return value: StudyRoomTodoDto
    */
    public StudyRoomTodoDto completeTodo(Long studyRoomId, Long todoId){
        StudyRoom studyRoom = extractOptionalRoom(studyRoomId);

        // 투두 가져오기
        StudyRoomTodo todo = extractOptionalTodo(todoId);
        todo.setCompleted(!todo.isCompleted());
        StudyRoomTodo savedTodo = studyRoomTodoRepository.save(todo);

        // dto 생성
        StudyRoomTodoDto result = todoToDto(savedTodo);
        // 웹소켓으로 전파
        String destination = "/topic/studyroom."+ studyRoomId +".todo";
        messagingTemplate.convertAndSend(destination, result);
        return result;
    }

    /*
    name : CreateTodoReaction
    URL: POST /api/study-rooms/{studyRoomId}/todos/{todoId}/reactions
    param : Long studyRoomId, Long todoId
    return value : Long reactionId
    */
    public Long createTodoReaction(Long studyRoomId, Long todoId, Long communityId){
        StudyRoomTodo todo = extractOptionalTodo(todoId);
        CommunityUser reactor = extractOptionalUser(communityId);

        Reaction reaction = Reaction.builder()
                .reaction("reaction")
                .reactor(reactor)
                .todo(todo)
                .build();
        Reaction savedReaction = reactionRepository.save(reaction);
        studyRoomTodoRepository.save(todo);

        ReactionDto result = ReactionDto.builder()
                .reactionId(savedReaction.getId())
                .creatorId(savedReaction.getReactor().getId())
                .todoId(todo.getId())
                .build();

        String destination = "/topic/studyroom."+ studyRoomId +".reaction";
        messagingTemplate.convertAndSend(destination, result);

        return  savedReaction.getId();
    }


    /*
    name : deleteTodoReaction
    url : DELETE /api/study-rooms/{studyRoomId}/todos/{todoId}/reactions/{reactionId}
    param : Long studyRoomId, Long todoId, Long reactionId
    return value : ResponseEntity 204
     */
    public void deleteTodoReaction(Long studyRoomId, Long todoId, Long reactionId, Long communityId){
        CommunityUser reactor = extractOptionalUser(communityId);

        Reaction reaction = reactionRepository.findById(reactionId).orElse(null);
        if(reaction == null) throw new RuntimeException("존재하지 않는 리액션입니다.");
        if(reactor.getId().equals(reaction.getReactor().getId())){
            reactionRepository.delete(reaction);
        }

        ReactionDto result = ReactionDto.builder()
                .reactionId(reactionId)
                .creatorId(null)
                .todoId(null)
                .build();

        String destination = "/topic/studyroom."+ studyRoomId +".reaction";
        messagingTemplate.convertAndSend(destination, result);

    }


    // ======= private logics ======= //

    private List<StudyRoomTodoDto> todoToDtoList(List<StudyRoomTodo> todos){
        List<StudyRoomTodoDto> result = new ArrayList<>();
        for(StudyRoomTodo st : todos){
            List<String> reactions = new ArrayList<>();
            for(Reaction r : st.getReactions()){
                reactions.add(r.getReaction());
            }

            StudyRoomTodoDto todoDto = StudyRoomTodoDto.builder()
                    .todoId(st.getId())
                    .creatorId(st.getCreator().getId())
                    .title(st.getTitle())
                    .description(st.getDescription())
                    .isCompleted(st.isCompleted())
                    .reactions(reactions)
                    .build();
            result.add(todoDto);
        }
        return result;
    }

    private StudyRoomTodoDto todoToDto(StudyRoomTodo savedTodo){
        List<String> reactions = new ArrayList<>();
        for(Reaction r : savedTodo.getReactions()){
            reactions.add(r.getReaction());
        }
        StudyRoomTodoDto todo = StudyRoomTodoDto.builder()
                .todoId(savedTodo.getId())
                .creatorId(savedTodo.getCreator().getId())
                .description(savedTodo.getDescription())
                .isCompleted(savedTodo.isCompleted())
                .reactions(reactions)
                .build();
        return todo;
    }

    private StudyRoom extractOptionalRoom(Long studyRoomId){
        Optional<StudyRoom> studyRoom = studyRoomRepository.findById(studyRoomId);
        return studyRoom.orElse(null);
    }

    private CommunityUser extractOptionalUser(Long communityId){
        Optional<CommunityUser> communityUser = communityUserRepository.findById(communityId);
        return communityUser.orElse(null);
    }

    private StudyRoomTodo extractOptionalTodo(Long todoId){
        Optional<StudyRoomTodo> todo = studyRoomTodoRepository.findById(todoId);
        return todo.orElse(null);
    }
}
