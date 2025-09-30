package GRWM.backend.controller.community;

import GRWM.backend.dto.community.CommunityUserBriefDto;
import GRWM.backend.dto.community.CommunityUserFullInfoDto;
import GRWM.backend.dto.community.FollowDto;
import GRWM.backend.dto.community.ProfileUpdateDto;
import GRWM.backend.entity.user.CustomUserDetails;
import GRWM.backend.service.community.CommunityUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/users/")
public class ProfileController {

    private final CommunityUserService communityUserService;

    /*
    함수명 : showUserProfile
    기능  : 사용자 프로필 조회
    매개변수: x
    반환값 : dto CommunityUserFullInfoDto
     */

    @GetMapping("/profile")
    public CommunityUserFullInfoDto showUserProfile(@PathVariable Long userId){
        return communityUserService.showUserProfile(userId);
    }




    /*
    함수명 : updateUserProfile
    기능  : 사용자 프로필 수정
    매개변수: dto username, profileImage, description
    반환값 : dto CommunityUserFullInfoDto
     */

    @PutMapping("/profile")
    public CommunityUserFullInfoDto updateUserProfile(@RequestBody ProfileUpdateDto dto, @AuthenticationPrincipal CustomUserDetails userDetails){
        return communityUserService.updateUserProfile(
                userDetails.getCommunityUserId(),
                dto);
    }



    /*
    함수명 : followUser
    기능  : 다른 사용자 팔로우
    매개변수: dto username, profileImage, description
    반환값 : dto CommunityUserFullInfoDto
     */

    @PostMapping("/{targetId}/follow")
    public ResponseEntity<Void> followUser(@PathVariable Long targetId,
                                           @AuthenticationPrincipal CustomUserDetails userDetails){
        communityUserService.followUser(userDetails.getCommunityUserId(), targetId);
        return ResponseEntity.ok().build();
    }

    /*
    함수명 : unfollowUser
    기능  : 기존에 팔로우하던 사용자 언팔로우
    매개변수: x
    반환값 : dto CommunityUserFullInfoDto
     */

    @DeleteMapping("/{targetId}/follow")
    public ResponseEntity<Void> unfollowUser(@PathVariable Long targetId,
                                           @AuthenticationPrincipal CustomUserDetails userDetails){
        communityUserService.unfollowUser(userDetails.getCommunityUserId(), targetId);
        return ResponseEntity.ok().build();
    }



    /*
    함수명 : getFollowerList
    기능  : 사용자를 팔로우하는 사람들 리스트
    매개변수: dto username, profileImage, description
    반환값 : dto CommunityUserFullInfoDto
     */

    @GetMapping("/{userId}/followers")
    public FollowDto getFollowerList(@PathVariable Long userId){
        FollowDto dto = new FollowDto(
        communityUserService.getFollowerList(userId),
                communityUserService.countFollower(userId));

        return dto;
    }


    /*
    함수명 : getFollowingList
    기능  : 사용자가 팔로잉하는 계정 리스트
    매개변수: dto username, profileImage, description
    반환값 : dto CommunityUserFullInfoDto
     */

    @GetMapping("/{userId}/following")
    public FollowDto getFollowingList(@PathVariable Long userId){
        FollowDto dto = new FollowDto(
        communityUserService.getFollowingList(userId),
                communityUserService.countFollowing(userId));
        return dto;
    }


    /*
    함수명 : blockUser
    기능  : 다른 계정 블락
    매개변수: dto username, profileImage, description
    반환값 : dto CommunityUserFullInfoDto
     */

    @PostMapping("/blocks/{targetId}")
    public ResponseEntity<Void> blockUser(@PathVariable Long targetId,
                                          @AuthenticationPrincipal CustomUserDetails userDetails){

        communityUserService.blockUser(userDetails.getCommunityUserId(), targetId);
        return ResponseEntity.ok().build();
    }


    /*
    함수명 : unblockUser
    기능  : 사용자 프로필 수정
    매개변수: dto username, profileImage, description
    반환값 : dto CommunityUserFullInfoDto
     */

    @DeleteMapping("/blocks/{targetId}")
    public ResponseEntity<Void> unblockUser(@AuthenticationPrincipal CustomUserDetails userDetails,
                                            @PathVariable Long targetId){

        communityUserService.unblockUser(userDetails.getCommunityUserId(), targetId);
        return ResponseEntity.ok().build();
    }


    /*
    함수명 : getBlockedUserList
    기능  : 블락한 사용자 리스트 조회
    매개변수: dto username, profileImage, description
    반환값 : dto CommunityUserFullInfoDto
     */

    @GetMapping("/blocks")
    public FollowDto getBlockedUserList(@AuthenticationPrincipal CustomUserDetails userDetails){
        FollowDto dto = new FollowDto(
        communityUserService.getBlockedUserList(userDetails.getCommunityUserId()),
                communityUserService.countBlockedUsers(userDetails.getCommunityUserId()));

        return dto;
    }

    // ======== count 반환 로직 ======= //



}
