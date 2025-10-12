package GRWM.backend.service.community;

import GRWM.backend.dto.community.CommunityUseListDto;
import GRWM.backend.dto.community.CommunityUserBriefDto;
import GRWM.backend.dto.community.CommunityUserFullInfoDto;
import GRWM.backend.dto.community.ProfileUpdateDto;
import GRWM.backend.entity.community.BlockList;
import GRWM.backend.entity.community.Following;
import GRWM.backend.entity.community.Post;
import GRWM.backend.entity.community.UserBadge;
import GRWM.backend.entity.user.CommunityUser;

import GRWM.backend.repository.community.BlockListRepository;
import GRWM.backend.repository.community.FollowingRepository;
import GRWM.backend.repository.user.CommunityUserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommunityUserService {

    private final CommunityUserRepository communityUserRepository;
    private final FollowingRepository followingRepository;
    private final BlockListRepository blockListRepository;


        /*
    함수명 : showUserProfile
    기능  : 사용자 프로필 조회
    매개변수: Long communityUserId
    반환값 : dto CommunityUserFullInfoDto
     */

    public CommunityUserFullInfoDto showUserProfile(Long communityUserId, Long targetId) {
        // user는 로그인한 사용자, target은 프로필 조회하는 상대방
        CommunityUser user = extractOptionalUser(communityUserId);
        CommunityUser target = extractOptionalUser(targetId);

        CommunityUserBriefDto briefDto = new CommunityUserBriefDto(
                target.getId(),
                target.getNickname(),
                target.getProfileImage()
        );

        List<Post> postList = target.getPostList();
        int postCount = postList.size();

        List<Following> followingList = target.getFollowingList();
        int followingCount = followingList.size();

        List<Following> followerList = target.getFollowerList();
        int followerCount = followerList.size();

        List<UserBadge> archivedBadgeList = target.getUserBadgeList();
        int archivedBadgeCount = archivedBadgeList.size();

        CommunityUserFullInfoDto dto = CommunityUserFullInfoDto.builder()
                .User(briefDto)
                .description(target.getDescription())
                .bannerImage(target.getBannerImage())
                .postCount(postCount)
                .followerCount(followerCount)
                .followingCount(followingCount)
                .archivedBadgeCount(archivedBadgeCount)
                .pinnedPostId(Optional.ofNullable(target.getPinnedPost())
                        .map(Post::getId) // Post 객체가 있으면 getId() 호출
                        .orElse(null)   // 메인 포스트
                )
                .relationship(getRelationship(user, target))
                .build();

        return dto;
    }






            /*
    함수명 : showCommunityUserBriefInfo
    기능  : 사용자 프로필 조회
    매개변수: x
    반환값 : dto CommunityUserFullInfoDto
     */

    public CommunityUserBriefDto showCommunityUserBriefInfo(Long communityId) {
        CommunityUser user = extractOptionalUser(communityId);
        return userToDto(user);
    }




    /*
    함수명 : updateUserProfile
    기능  : 사용자 프로필 수정
    매개변수: dto username, profileImage, description
    반환값 : dto CommunityUserFullInfoDto
     */

    public CommunityUserFullInfoDto updateUserProfile(Long communityId, ProfileUpdateDto dto) {
        // 사용자 객체 불러오기

        CommunityUser user = extractOptionalUser(communityId);


        // dto에 담긴 값으로 업데이트

        user.setNickname(dto.getNickname());
        user.setProfileImage(dto.getProfileImage());
        user.setDescription(dto.getDescription());
        user.setBannerImage(dto.getBannerImage());

        communityUserRepository.flush();

        CommunityUserBriefDto briefDto = new CommunityUserBriefDto(user.getId(), user.getNickname(), user.getProfileImage());
        CommunityUserFullInfoDto updatedDto = CommunityUserFullInfoDto.builder()
                .User(briefDto)
                .description(user.getDescription())
                .bannerImage(user.getBannerImage())
                .postCount(user.countPost())
                .followerCount(user.countFollower())
                .followingCount(user.countFollowing())
                .archivedBadgeCount(user.countArchivedBadge())
                .pinnedPostId(Optional.ofNullable(user.getPinnedPost())
                        .map(Post::getId) // Post 객체가 있으면 getId() 호출
                        .orElse(null)   // 메인 포스트
                )
                .build();

        return updatedDto;

    }



    /*
    함수명 : followUser
    기능  : 다른 사용자 팔로우
    매개변수: dto username, profileImage, description
    반환값 : int
     */

    public int followUser(Long communityId, Long targetId) {

        if(followingRepository.existByFollowingAndFollower(
                extractOptionalUser(targetId),
                extractOptionalUser(communityId))
        ){
            // 팔로잉 관계 데이터가 이미 존재한다면
            throw new RuntimeException("이미 팔로우한 사용자입니다.");
        }

        // 팔로잉 리스트 생성
        Following following = new Following(
                extractOptionalUser(targetId),
                extractOptionalUser(communityId)
        );

        // 저장
        followingRepository.save(following);
        return countFollowing(communityId);
    }


    /*
    함수명 : unfollowUser
    기능  : 다른 사용자 언팔로우
    매개변수: communityId, targetId
    반환값 : dto CommunityUserFullInfoDto
     */

    public int unfollowUser(Long communityId, Long followingId) {

        // Id로 커뮤니티 객체 불러오기;


        Following following = followingRepository.findByFollowingAndFollower(

                extractOptionalUser(followingId),
                extractOptionalUser(communityId)
        );
        followingRepository.delete(following);
        return countFollowing(communityId);
    }



    /*
    함수명 : getFollowerList
    기능  : 팔로워 리스트 가져오기
    매개변수: Long communityId
    반환값 : int
     */

    public List<CommunityUserBriefDto> getFollowerList(Long communityId) {
        // communityId로 followingList 조회하여.
        List<Following> followerList = followingRepository.findByFollowing(extractOptionalUser(communityId));

        List<CommunityUserBriefDto> dtoList = new ArrayList<>();
        for (Following t : followerList) {
            dtoList.add(userToDto(t.getFollower()));
        }

        return dtoList;

    }





    /*
    함수명 : getFollowingList
    기능  : 사용자의 팔로잉 리스트 가져오기.
    매개변수: Long communityId
    반환값 : List<CommunityUserBriefDto>
     */

    public List<CommunityUserBriefDto> getFollowingList(Long communityId) {

        List<Following> followingList = followingRepository.findByFollower(extractOptionalUser(communityId));

        List<CommunityUserBriefDto> dtoList = new ArrayList<>();
        for (Following t : followingList) {
            dtoList.add(userToDto(t.getFollowing()));
        }

        return dtoList;
    }



    /*
    함수명 : blockUser
    기능  : 사용자 프로필 수정
    매개변수: dto username, profileImage, description
    반환값 : dto CommunityUserFullInfoDto
     */

    public void blockUser(Long communityUserId, Long targetId) {

        // 블락 리스트의 새 칼럼 생성
        BlockList block = new BlockList(
                extractOptionalUser(communityUserId),
                extractOptionalUser(targetId)
        );

        // 저장
        blockListRepository.save(block);
    }

    /*
    함수명 : unblockUser
    기능  : 사용자 블락 해제
    매개변수: dto username, profileImage, description
    반환값 : void
     */

    public void unblockUser(Long communityUserId, Long targetId) {

        BlockList block = blockListRepository.findByBlockerAndBlockedUser(
                extractOptionalUser(communityUserId),
                extractOptionalUser(targetId)
        );

        blockListRepository.delete(block);

    }


    /*
    함수명 : getBlockedUserList
    기능  : 블락한 사용자 목록 가져오기
    매개변수: Long communityId
    반환값 : dto CommunityUserBriefDto
     */

    public List<CommunityUserBriefDto> getBlockedUserList(Long communityId) {
        List<BlockList> blockList = blockListRepository.findByBlocker(extractOptionalUser(communityId));

        List<CommunityUserBriefDto> dtoList = new ArrayList<>();
        for (BlockList t : blockList) {

            dtoList.add(userToDto(t.getBlockedUser()));
        }

        return dtoList;
    }


    // ======= 검색 로직 ======= //



    /*
    함수명 : searchUser
    기능 : 사용자 검색
    매개변수 : String keyword
    반환값 : UserBriefDto List,  유저 목록 개수
     */

    public CommunityUseListDto searchUser(String keyword, Pageable pageable) {

        // pageable 객체 생성
        Pageable p = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        // 키워드로 nickname 검색하는 리포지토리 로직 생성
        Slice<CommunityUser> userSlice = communityUserRepository.findByNicknameContaining(keyword, p);
        List<CommunityUser> userList = userSlice.getContent();

        List<CommunityUserBriefDto> dtoList = new ArrayList<>();
        for (CommunityUser t : userList) {
            dtoList.add(userToDto(t));
        }

        return new CommunityUseListDto(dtoList, userSlice.hasNext());
    }

    public String findNicknameById(Long communityId){
        return extractOptionalUser(communityId).getNickname();
    }




    // ======= count 로직; ======= //

    public int countFollowing(Long communityId) {
        return extractOptionalUser(communityId).countFollowing();
    }

    public int countFollower(Long communityId) {
        return extractOptionalUser(communityId).countFollower();
    }

    public int countBlockedUsers(Long communityId) {
        List<BlockList> blockList = blockListRepository.findByBlocker(extractOptionalUser(communityId));
        return blockList.size();
    }


    private CommunityUser extractOptionalUser(Long communityId) {
        Optional<CommunityUser> optionalUser = communityUserRepository.findById(communityId);
        CommunityUser user = null;


        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            throw new RuntimeException("존재하지 않는 사용자입니다.");
        }
    }


    private CommunityUserBriefDto userToDto(CommunityUser user) {

        CommunityUserBriefDto newDto = new CommunityUserBriefDto(
                user.getId(),
                user.getNickname(),
                user.getProfileImage()
        );

        return newDto;

    }

    private String getRelationship(CommunityUser user, CommunityUser target) {
        if(user.equals(target)) return null;
        else if (followingRepository.findByFollowingAndFollower(user, target) != null
            // 내가 팔로우됨, 타겟이 팔로워
        ) return "isFollowingMe";
        else if (followingRepository.findByFollowingAndFollower(target, user) != null
            // 내가 타겟의 팔로워
        ) return "followedByMe";

        else if (blockListRepository.findByBlockerAndBlockedUser(user, target) != null
            // 내가 target 을 블락함
        ) return "isBlockedByMe";
        else if (blockListRepository.findByBlockerAndBlockedUser(target, user) != null
            // 내가 target 에게 블락됨
        ) return "isBlockingMe";
        else return "noRelationship";

    }
}