package GRWM.backend.controller.community;


import GRWM.backend.dto.community.PostDto;
import GRWM.backend.entity.user.CustomUserDetails;
import GRWM.backend.service.community.HashtagService;
import GRWM.backend.service.community.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")

public class HashtagController {

    private final HashtagService hashtagService;
    private final PostService postService;

        /*
    함수명 :
    기능  :
    매개변수 :
    반환값 :
     */

    /*
    함수명 : createHashtag
    기능  : 해시태그 생성
    매개변수 : String hashtag
    반환값 : responseEntity
     */
    @PostMapping("/hashtags")
    public Long createHashtag(@RequestParam String keyword){
        return hashtagService.createHashtag(keyword);

    }

    /*
    함수명 : getSubscribedHashtagList
    기능  : 구독한 해시태그 리스트 불러오기
    매개변수 : Long communityId
    반환값 : List<Hashtag>
     */ // 구독한 해시태그 리스트와 해시태그 개수는 따로 만들어야.

    @GetMapping("/users/subscribed-hashtags")
    public List<String> getSubscribedHashtagList(@AuthenticationPrincipal CustomUserDetails userDetails){
        return hashtagService.getSubscribedHashtagList(userDetails.getCommunityUserId());
    }


        /*
    함수명 : subscribeHashtag
    기능  : 해시태그 구독하기
    매개변수 : Long communityId, tagId
    반환값 : ResponseEntity
     */

    @PostMapping("/users/{userId}/subscribed-hashtags/{tagId}")
    public ResponseEntity<Void> subscribeHashtag(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                 @PathVariable Long tagId){
        hashtagService.subscribeHashtag(userDetails.getCommunityUserId(), tagId);
        return ResponseEntity.ok().build();
    }


    /*
    함수명 : unsubscribeHashtag
    기능  : 해시태그 구독 해제
    매개변수 : Long communityId, Long tagId
    반환값 : ResponseEntity
     */

    @DeleteMapping("/users/subscribed-hashtags/{tagId}")
    public ResponseEntity<Void> unsubscribeHashtag(@PathVariable Long tagId,
                                                   @AuthenticationPrincipal CustomUserDetails userDetails){
        hashtagService.unsubscribeHashtag(userDetails.getCommunityUserId(), tagId);
        return ResponseEntity.ok().build();
    }


    /*
    함수명 : updateHashtagOrder
    기능  : 사용자에게 보이는 해시태그 목록의 순서 변경
    매개변수 : 해시태그 목록
    반환값 : 해시태그 목록
     */



    /*
    함수명 : getSubscribedHashtagPostList
    기능  : 사용자가 구독하는 해시태그의 개시글 조회
    매개변수 : Long communityId
    반환값 : List<PostDto>
     */

    @GetMapping("/users/subscribed-hashtags/posts")
    public List<PostDto> getSubscribedHashtagPosts(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                   @PageableDefault(size = 20) Pageable pageable){
        return postService.getSubscribedHashtagPostList(userDetails.getCommunityUserId(), pageable);

    }





        /*
    함수명 : getSubscribedHashtagCount
    기능  : 구독한 해시태그 리스트 개수 불러오기
    매개변수 : Long communityId
    반환값 : int
     */

    @GetMapping("/users/subscribed-hashtags/count")
    public int getSubscribedHashtagCount(@AuthenticationPrincipal CustomUserDetails userDetails){

        return hashtagService.getSubscribedHashtagCount(userDetails.getCommunityUserId());

    }



    //------ 해시태그 id 반환 로직 ------//

    /*
    함수명 : getHashtagId
    기능  : 해시태그 아이디를 반환한다.
    매개변수 : String keyword
    반환값 : Long id
     */
    @GetMapping("/hashtag")
    public ResponseEntity<Long> getHashtagId(@RequestParam String keyword){
        return ResponseEntity.ok(hashtagService.getHashtagId(keyword));
    }



}
