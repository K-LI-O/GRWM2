
package GRWM.backend.controller.community;

import GRWM.backend.dto.community.PostCreateDto;
import GRWM.backend.dto.community.PostDto;
import GRWM.backend.dto.community.PostListDto;
import GRWM.backend.dto.community.PostUpdateDto;
import GRWM.backend.entity.user.CustomUserDetails;
import GRWM.backend.service.community.CommentService;
import GRWM.backend.service.community.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/community-posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final CommentService commentService;



    // =======  CRUD 로직 ======= //

    /*
    함수명 : createPost
    기능 : 커뮤니티 게시글 생성
    매개변수 : PostCreateDto
    반환값: ResponseBody, Long postId

 */

    @PostMapping()
    public Long createPost(@RequestBody PostCreateDto dto,
                           @AuthenticationPrincipal CustomUserDetails userDetails){
        return postService.createPost(userDetails.getCommunityUserId(), dto);
    }




    /*
    함수명 : showPostDetail
    기능 : 게시글 하나의 정보 불러오기
    매개변수 : path variable Long postId
    반환값: Post 정보(유저, 포스트 자체 정보, 리액션), commentList(아이디, 컨텐트, 작성 날짜)
    */

    @GetMapping("/{postId}")
    public PostDto showPostDetail(@PathVariable Long postId){
        return postService.showPostDetail(postId);
    }



    /*
    함수명 : updatePost
    기능 : 게시글 수정하기
    매개변수 : path variable Long postId
    반환값: Dto: 수정된 Post 정보(유저, 포스트 자체 정보, 리액션), commentList(아이디, 컨텐트, 작성 날짜)

    */

    @PutMapping("/{postId}")
    public PostDto updatePost(@PathVariable Long postId,
                              @RequestBody PostUpdateDto dto){
        return postService.updatePost(postId, dto);
    }



    /*
    함수명 : deletePost
    기능 : 게시글 삭제하기
    매개변수 : path variable Long postId
    반환값: Dto: responseEntity 200 OK
    */

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId){
        postService.deletePost(postId);
        return ResponseEntity.ok().build();
    }



    // ======= pageable 사용 로직들 ======= //

    /*
    함수명 : showTimelinePostList
    기능 : 타임라인 리스트 불러오기; 팔로우하는 사람의 최근 포스트
    매개변수 : communityId 추가 필요
    반환값:
    포스트 리스트,
    좋아요 개수 리스트,
    boolean - 더 있는가?
    */


    @GetMapping()
    public PostListDto showTimelinePostList(@AuthenticationPrincipal CustomUserDetails userDetails,
                                              @PageableDefault(size = 20) Pageable pageable){

        return postService.showTimelinePostList(userDetails.getCommunityUserId(), pageable);
    }


   /*
    함수명 : getUserPosts
    기능 : 각 계정별 포스트를 리스트로 반환;
    매개변수 : path variable Long postId
    반환값: Dto: responseEntity 200 OK
    */

    @GetMapping("/user/{communityId}")
    public PostListDto getUserPosts(@PathVariable Long communityId,
                                    @PageableDefault(size = 20) Pageable pageable){
        return postService.getUserPosts(communityId, pageable);
    }


    // ======== 메인 게시물 설정 로직 ======= //

    /*
    함수명 : pinPost
    기능 : 게시글 메인에 고정하기
    매개변수 : path variable Long postId
    반환값: Dto: responseEntity 200 OK

    */

    @PatchMapping("/{postId}/pin")
    public ResponseEntity<Void> pinPost(@PathVariable Long postId,
                                        @AuthenticationPrincipal CustomUserDetails userDetails){
        postService.pinPost(userDetails.getCommunityUserId(), postId);
        return ResponseEntity.ok().build();
    }

    /*
    함수명 : unpinPost
    기능 : 게시글 메인에서 고정 해제하기
    매개변수 : path variable Long postId
    반환값: Dto: responseEntity 200 OK

*/

    @PatchMapping("/{postId}/unpin")
    public ResponseEntity<Void> unpinPost(@PathVariable Long postId,
                                        @AuthenticationPrincipal CustomUserDetails userDetails){
        postService.unpinPost(userDetails.getCommunityUserId(), postId);
        return ResponseEntity.ok().build();
    }



}
