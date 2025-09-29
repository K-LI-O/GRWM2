package GRWM.backend.controller.community;

import GRWM.backend.dto.community.CommentCreateDto;
import GRWM.backend.dto.community.CommentDto;
import GRWM.backend.dto.community.CommentUpdateDto;
import GRWM.backend.entity.user.CustomUserDetails;
import GRWM.backend.service.community.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/community-posts/{postId}/comments")
public class CommentController {


    private final CommentService commentService;


    /*
    함수명 :
    기능 :
    매개변수 :
    반환값 :
     */




    /*
    함수명 : createComment
    기능 : 포스트에 댓글 작성
    매개변수 : Long postId, CommentCreateDto
    반환값 : CommentDto
     */

    @PostMapping()
    public CommentDto createComment(@PathVariable Long postId,
                                    @RequestBody CommentCreateDto dto,
                                    @AuthenticationPrincipal CustomUserDetails userDetails){

        return commentService.createComment(userDetails.getCommunityUserId(), postId, dto);

    }



    /*
    함수명 : updateComment
    기능 : 댓글을 수정하고, 수정 여부를 표기함
    매개변수 : Long commentId, String content, boolean isPrivate;
    반환값 : CommentDto
     */

    @PutMapping("/{commentId}")
    public CommentDto updateComment(@PathVariable Long commentId,
                                    @RequestBody CommentUpdateDto dto,
                                    @AuthenticationPrincipal CustomUserDetails userDetails){

        return commentService.updateComment(userDetails.getCommunityUserId(), commentId, dto);
    }



    /*
    함수명 : deleteComment
    기능 : 댓글 삭제
    매개변수 : commentId
    반환값 : postId, commentId(사실 댓글 아이디만 있어도 됨;;)
     */

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId){
        commentService.deleteComment(commentId);

        return ResponseEntity.ok().build();
    }




    /*
    함수명 : getCommentList
    기능 : 한 게시글의 댓글 리스트 반환
    매개변수 : communityId, postId
    반환값 : List<CommentDto>, 댓글 개수
     */

    @GetMapping()
    public List<CommentDto> getCommentList(@AuthenticationPrincipal CustomUserDetails userDetails,
                                           @PathVariable Long postId){

        return commentService.getCommentList(userDetails.getCommunityUserId(), postId);

    }


    /*
    함수명 : getCommentCount
    기능 :  한 게시글의 댓글 개수 반환
    매개변수 : postId
    반환값 : int commentCount;
     */

    @GetMapping("/comment-count")
    public int getCommentCount(@PathVariable Long postId){
        return commentService.getCommentCount(postId);
    }


}
