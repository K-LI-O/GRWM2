
package GRWM.backend.controller.community;

import GRWM.backend.dto.community.PostCreateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/controller-posts")
@RequiredArgsConstructor
public class PostController {
/*
    // private final 어쩌구.

    /*
    함수명 : createPost
    기능 : 커뮤니티 게시글 생성
    매개변수 : PostCreateDto
    반환값: ResponseBody, Long postId

    @PostMapping
    public ResponseEntity<Long> createPost(@ResponseBody PostCreateDto){

    }

    /*
    함수명 : showTimelinePostList
    기능 : 타임라인 리스트 불러오기; 팔로우하는 사람의 최근 포스트
    매개변수 : communityId 추가 필요
    반환값:
    포스트 리스트,
    좋아요 개수 리스트,
    boolean - 더 있는가?


    @GetMapping("/{communityId}?limit=40")
    public ResponseEntity<Long> showTimelinePostList(){

    }


    /*
    함수명 : showPostDetail
    기능 : 게시글 하나의 정보 불러오기
    매개변수 : path variable Long postId
    반환값: Post 정보(유저, 포스트 자체 정보, 리액션), commentList(아이디, 컨텐트, 작성 날짜)


    @GetMapping("/{postId}")
    public ResponseEntity<Long> showPostDetail(){

    }



    /*
    함수명 : updatePost
    기능 : 게시글 수정하기
    매개변수 : path variable Long postId
    반환값: Dto: 수정된 Post 정보(유저, 포스트 자체 정보, 리액션), commentList(아이디, 컨텐트, 작성 날짜)


    @PutMapping("/{postId}")
    public ResponseEntity<Long> updatePost(){

    }


    /*
    함수명 : deletePost
    기능 : 게시글 삭제하기
    매개변수 : path variable Long postId
    반환값: Dto: responseEntity 200 OK


    @DeleteMapping("/{postId}")
    public ResponseEntity<Long> deletePost(){

    }

    /*
    함수명 : deletePost
    기능 : 게시글 삭제하기
    매개변수 : path variable Long postId
    반환값: Dto: responseEntity 200 OK


    @DeleteMapping("/{postId}")
    public ResponseEntity<Long> deletePost(){

    }

    /*
    함수명 : pinPost
    기능 : 게시글 메인에 고정하기
    매개변수 : path variable Long postId
    반환값: Dto: responseEntity 200 OK


    @DeleteMapping("/{postId}/pin")
    public ResponseEntity<Long> pinPost(){

    }

    /*
    함수명 : unpinPost
    기능 : 게시글 메인에서 고정 해제하기
    매개변수 : path variable Long postId
    반환값: Dto: responseEntity 200 OK

*/ /*
    @DeleteMapping("/{postId}/unpin")
    public ResponseEntity<Long> pinPost(){

    }

*/


}
