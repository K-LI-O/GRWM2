package GRWM.backend.service.community;

import GRWM.backend.dto.community.CommunityUserBriefDto;
import GRWM.backend.dto.community.CommunityUserFullInfoDto;
import GRWM.backend.dto.community.ProfileUpdateDto;
import GRWM.backend.entity.community.Following;
import GRWM.backend.entity.community.Post;
import GRWM.backend.entity.community.UserBadge;
import GRWM.backend.entity.user.CommunityUser;

import GRWM.backend.repository.community.FollowingRepository;
import GRWM.backend.repository.user.CommunityUserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommunityUserService {

    private final CommunityUserRepository communityUserRepository;
    private final FollowingRepository followingRepository;


        /*
    함수명 : showUserProfile
    기능  : 사용자 프로필 조회
    매개변수: Long communityUserId
    반환값 : dto CommunityUserFullInfoDto
     */

    public CommunityUserFullInfoDto showUserProfile(Long communityUserId){

        CommunityUser user = extractOptionalUser(communityUserId);

        CommunityUserBriefDto briefDto = new CommunityUserBriefDto(
                user.getId(),
                user.getNickname(),
                user.getProfileImage()
        );

        List<Post> postList = user.getPostList();
        int postCount = postList.size();

        List<Following> followingList = user.getFollowingList();
        int followingCount = followingList.size();

        List<Following> followerList = user.getFollowerList();
        int followerCount = followerList.size();

        List<UserBadge> archivedBadgeList = user.getUserBadgeList();
        int archivedBadgeCount = archivedBadgeList.size();

        CommunityUserFullInfoDto dto = new CommunityUserFullInfoDto(
                briefDto,
                user.getDescription(),
                user.getBannerImage(),
                postCount,
                followerCount,
                followingCount,
                archivedBadgeCount, // 뱃지 개수
                user.getPinnedPost().getId()// 메인 포스트
                );

        return dto;
    }




    /*
    함수명 : updateUserProfile
    기능  : 사용자 프로필 수정
    매개변수: dto username, profileImage, description
    반환값 : dto CommunityUserFullInfoDto
     */

    public void updateUserProfile(Long communityId, ProfileUpdateDto dto){
        // 사용자 객체 불러오기

        CommunityUser user = extractOptionalUser(communityId);


        // dto에 담긴 값으로 업데이트

        user.setNickname(dto.getNickname());
        user.setProfileImage(dto.getProfileImage());
        user.setDescription(dto.getDescription());
        user.setBannerImage(dto.getBannerImage());

        communityUserRepository.flush();

    }



    /*
    함수명 : followUser
    기능  : 다른 사용자 팔로우
    매개변수: dto username, profileImage, description
    반환값 : dto CommunityUserFullInfoDto
     */

    public void followUser(Long communityId, Long targetId){

        // 팔로잉 리스트 생성
        Following following = new Following(
                extractOptionalUser(communityId),
                extractOptionalUser(targetId)
        );

        // 저장
        followingRepository.save(following);
    }


    /*
    함수명 : unfollowUser
    기능  : 다른 사용자 언팔로우
    매개변수: communityId, targetId
    반환값 : dto CommunityUserFullInfoDto
     */

    public void unfollowUser(Long communityId, Long followingId){

        // Id로 커뮤니티 객체 불러오기;


        Following following = followingRepository.findByFollowingAndFollower(
                extractOptionalUser(communityId),
                extractOptionalUser(followingId)
                );
        followingRepository.delete(following);
    }



    /*
    함수명 : getFollowerList
    기능  : 팔로워 리스트 가져오기
    매개변수: dto username, profileImage, description
    반환값 : List<CommunityUserBriefDto>


    public List<CommunityUserBriefDto> getFollowerList(Long communityId){
        // communityId로 followingList 조회하여.
    }


     */


    /*
    함수명 : getFollowingList
    기능  : 사용자 프로필 수정
    매개변수: dto username, profileImage, description
    반환값 : dto CommunityUserFullInfoDto
     */



    /*
    함수명 : blockUser
    기능  : 사용자 프로필 수정
    매개변수: dto username, profileImage, description
    반환값 : dto CommunityUserFullInfoDto
     */



    /*
    함수명 : unblockUser
    기능  : 사용자 프로필 수정
    매개변수: dto username, profileImage, description
    반환값 : dto CommunityUserFullInfoDto
     */


    /*
    함수명 : getBlockedUserList
    기능  : 사용자 프로필 수정
    매개변수: dto username, profileImage, description
    반환값 : dto CommunityUserFullInfoDto
     */

    private CommunityUser extractOptionalUser(Long communityId){
        Optional<CommunityUser> optionalUser = communityUserRepository.findById(communityId);
        CommunityUser user = null;

        try {
            if (optionalUser.isPresent()) {
                user = optionalUser.get();
            }
        } catch (RuntimeException e) {
            throw new RuntimeException("존재하지 않는 프로필입니다.");
        }

        return user;
    }


}
