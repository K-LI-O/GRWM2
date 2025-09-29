package GRWM.backend.controller.community;

import GRWM.backend.dto.community.CommunityUserBriefDto;
import GRWM.backend.entity.user.CustomUserDetails;
import GRWM.backend.service.community.LikedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/community-posts/{postId}/like")
public class LikedController {

    private final LikedService likedService;

    /*
    함수명 : likePost
    기능 : 게시글에 좋아요 표시
    매개변수 : Long postId, Long communityId
    반환값 : responseEntity,
     */

    @PostMapping()
    public ResponseEntity<Void> likePost(@PathVariable Long postId,
                                         @AuthenticationPrincipal CustomUserDetails userDetails){

        likedService.likePost(postId, userDetails.getCommunityUserId());
        return ResponseEntity.ok().build();

    }


    /*
    함수명 : cancelLike
    기능 : 게시글의 좋아요 취소하기
    매개변수 : Long postId, Long communityId
    반환값 : responseEntity
     */

    @DeleteMapping()
    public ResponseEntity<Void> cancelLike(@PathVariable Long postId,
                                           @AuthenticationPrincipal CustomUserDetails userDetails){
        likedService.cancelLike(postId, userDetails.getCommunityUserId());

        return ResponseEntity.ok().build();
    }




    /*
    함수명 : getPostLikedUserList
    기능 : 게시글에 좋아요를 누른 사용자 리스트 반환
    매개변수 : postId
    반환값 : List<communityUserBriefDto>
     */

    @GetMapping("/liked-users")
    public List<CommunityUserBriefDto> getPostLikedUserList(@PathVariable Long postId){
        return likedService.getPostLikedUserList(postId);
    }

    /*
    함수명 : countLikes
    기능 : 게시글의 좋아요 개수 반환
    매개변수 : Long postId
    반환값 : int countLikes
     */

    @GetMapping("/like-count")
    public int countLikes(@PathVariable Long postId){
        return likedService.countLikes(postId);
    }



}
