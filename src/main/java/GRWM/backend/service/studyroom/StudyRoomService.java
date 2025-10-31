package GRWM.backend.service.studyroom;

import GRWM.backend.dto.community.CommunityUserBriefDto;
import GRWM.backend.dto.studyroom.*;
import GRWM.backend.entity.studyroom.Reaction;
import GRWM.backend.entity.studyroom.StudyRoom;
import GRWM.backend.entity.studyroom.StudyRoomMember;
import GRWM.backend.entity.studyroom.StudyRoomTodo;
import GRWM.backend.entity.user.CommunityUser;
import GRWM.backend.repository.studyroom.StudyRoomMemberRepository;
import GRWM.backend.repository.studyroom.StudyRoomRepository;
import GRWM.backend.repository.user.CommunityUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class StudyRoomService {

    private final StudyRoomRepository studyRoomRepository;
    private final CommunityUserRepository communityUserRepository;
    private final StudyRoomMemberRepository studyRoomMemberRepository;

    /*
    name : createStudyRoom
    URL: POST /api/study-rooms
    param : StudyRoomCreateDto, UserDetails
    return value : Long studyRoomId
    */

    public Long createStudyRoom(StudyRoomCreateDto dto, Long communityId){
        // 스터디룸 및 객체 생성
        // 스터디룸 멤버 객체 생성

        StudyRoom studyRoom = StudyRoom.builder()
                .name(dto.getName())
                .creator(extractOptionalUser(communityId))
                .category(dto.getCategory())
                .description(dto.getDescription())
                .duration(dto.getDuration())
                .extensionTime(dto.getExtensionTime())
                .isActive(true)
                .build();
        StudyRoom savedRoom = studyRoomRepository.save(studyRoom);

        StudyRoomMember studyRoomMember = StudyRoomMember.builder()
                .studyRoom(savedRoom)
                .user(extractOptionalUser(communityId))
                .build();
        studyRoomMemberRepository.save(studyRoomMember);

        return savedRoom.getId();
    }


    /*
    name : getStudyRoomList
    URL: GET /api/study-rooms
    param : int page, int limit(request param)
    return value :  StudyRoomListDto
{
List<StudyRoomBriefDto> studyRooms;
int totalElement; 스터디룸의 총 개수
int currentPage; 페이징 시 현재 보내는 페이지
int totalPages; 전체 페이지 개수 (페이징 관련 파라미터)
}
    */

    public StudyRoomListDto getStudyRoomList(int page, int limit){
        // pageable 생성
        Pageable pageable = PageRequest.of(page, limit);
        Page<StudyRoom> studyRooms = studyRoomRepository.findAll(pageable);

        // DTO 에 담아 반환
        StudyRoomListDto dto = StudyRoomListDto.builder()
                .studyRooms(getStudyRoomDtoList(studyRooms.getContent()))
                .currentPage(studyRooms.getNumber())
                .totalElement(studyRooms.getTotalElements())
                .totalPages(studyRooms.getTotalPages())
                .build();
        return dto;
    }

        /*
    name : joinStudyRoom
    URL: POST /api/study-rooms/{studyRoomId}/join
    param : Long studyRoomId;
    return value : ResponseEntity<Boolean>
    */
    public boolean joinStudyRoom(Long studyRoomId, Long communityId){
        StudyRoom studyRoom = extractOptionalRoom(studyRoomId);
        CommunityUser user = extractOptionalUser(communityId);

        // 스터디룸이 active 한지 검증.
        if(!studyRoom.isActive()) return false;
        // 이미 존재하는 멤버인지 검증
        for(StudyRoomMember sm : studyRoom.getMembers()){
            if(sm.getUser().getId().equals(user.getId())) return false;
        }

        // 검증된 경우 멤버로 저장;
        StudyRoomMember studyRoomMember = StudyRoomMember.builder()
                .studyRoom(studyRoom)
                .user(extractOptionalUser(communityId))
                .build();

        studyRoomMemberRepository.save(studyRoomMember);
        return true;
    }


    /*
    name : getStudyRoomDetail
    URL: GET /api/study-rooms/{studyRoomId}
    param : Long studyRoomId
    return value : studyRoomDetailDto
{
StudyRoomDto studyRoom;
currentUserStatus: "joined" | "owner";
}
    */
    public StudyRoomDetailDto getStudyRoomDetail(Long studyRoomId, Long communityId){
        // 스터디룸 객체 불러오기 및 isActive 확인
        StudyRoom studyRoom = extractOptionalRoom(studyRoomId);

        // 사용자 owner | joined 확인
        String currentUserState;
        if(studyRoom.getCreator().getId().equals(communityId)) currentUserState = "owner";
        else currentUserState = "joined";

        List<CommunityUserBriefDto> users = new ArrayList<>();
        for(StudyRoomMember sm : studyRoom.getMembers()){
            users.add(userToDto(sm.getUser()));
        }

        // 스터디룸 DTO 생성
        StudyRoomDto studyRoomDto = StudyRoomDto.builder()
                .name(studyRoom.getName())
                .creator(userToDto(studyRoom.getCreator()))
                .category(studyRoom.getCategory())
                .description(studyRoom.getDescription())
                .users(users)
                .duration(studyRoom.getDuration())
                .extensionTime(studyRoom.getExtensionTime())
                .todoList(todoToDtoList(studyRoom.getTodoList()))
                .extensionCount(studyRoom.getExtensionCount())
                .build();

        StudyRoomDetailDto dto = StudyRoomDetailDto.builder()
                .studyRoom(studyRoomDto)
                .currentUserStatus(currentUserState)
                .build();

        return dto;
    }

    /*
    name : GoOutStudyRoom
    URL: POST /api/study-rooms/{studyRoomId}/leave
    param : Long studyRoomId;
    return value : ResponseEntity<Boolean>
     */
    public void goOutStudyRoom(Long studyRoomId, Long communityId){
        // 스터디룸 객체 가져오기
        StudyRoom studyRoom = extractOptionalRoom(studyRoomId);
        for(StudyRoomMember sm : studyRoom.getMembers()){
            if(sm.getUser().getId().equals(communityId)){
                studyRoomMemberRepository.delete(sm);
            }
        }
    }

    // ======= private logics ======= //

    private CommunityUser extractOptionalUser(Long communityId){
        Optional<CommunityUser> communityUser = communityUserRepository.findById(communityId);
        return communityUser.orElse(null);
    }

    private StudyRoom extractOptionalRoom(Long studyRoomId){
        Optional<StudyRoom> studyRoom = studyRoomRepository.findById(studyRoomId);
        return studyRoom.orElse(null);
    }

    private List<StudyRoomBriefDto> getStudyRoomDtoList(List<StudyRoom> studyRooms){
        List<StudyRoomBriefDto> result = new ArrayList<>();
        for(StudyRoom s : studyRooms){
            // CommunityUserDto 만들기
            CommunityUser creator = s.getCreator();
            CommunityUserBriefDto creatorDto = CommunityUserBriefDto.builder()
                    .communityId(creator.getId())
                    .nickname(creator.getNickname())
                    .profileImage(creator.getProfileImage())
                    .build();

            StudyRoomBriefDto dto = StudyRoomBriefDto.builder()
                    .id(s.getId())
                    .name(s.getName())
                    .creator(creatorDto)
                    .category(s.getCategory())
                    .description(s.getDescription())
                    .build();
            result.add(dto);
        }
        return result;
    }

    private CommunityUserBriefDto userToDto(CommunityUser user) {
        CommunityUserBriefDto newDto = new CommunityUserBriefDto(
                user.getId(),
                user.getNickname(),
                user.getProfileImage()
        );
        return newDto;
    }

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
}
