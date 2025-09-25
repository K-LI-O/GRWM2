package GRWM.backend.controller.community;

import GRWM.backend.entity.user.CustomUserDetails;
import GRWM.backend.service.community.CommunityUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/community")
public class ProfileController {

    private final CommunityUserService communityUserService;

    /*
    함수명 : showUserProfile
    기능  : 사용자 프로필 조회
    매개변수: x
    반환값 : dto CommunityUserFullInfoDto
     */

    public void showUserProfile(@AuthenticationPrincipal CustomUserDetails userDetails){

    }




    /*
    함수명 : updateUserProfile
    기능  : 사용자 프로필 수정
    매개변수: dto username, profileImage, description
    반환값 : dto CommunityUserFullInfoDto
     */



    /*
    함수명 : followUser
    기능  : 다른 사용자 팔로우
    매개변수: dto username, profileImage, description
    반환값 : dto CommunityUserFullInfoDto
     */


    /*
    함수명 : unfollowUser
    기능  : 사용자 프로필 조회
    매개변수: x
    반환값 : dto CommunityUserFullInfoDto
     */




    /*
    함수명 : getFollowerList
    기능  : 사용자 프로필 수정
    매개변수: dto username, profileImage, description
    반환값 : dto CommunityUserFullInfoDto
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



}
