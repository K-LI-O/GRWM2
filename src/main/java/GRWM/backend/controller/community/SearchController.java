package GRWM.backend.controller.community;

import GRWM.backend.dto.community.CommunityUseListDto;
import GRWM.backend.dto.community.PostListDto;
import GRWM.backend.service.community.CommunityUserService;
import GRWM.backend.service.community.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
public class SearchController {

    // private final 어쩌구....
    private final PostService postService;
    private final CommunityUserService communityUserService;

    /*
    함수명 :
    기능 :
    매개변수 :
    반환값 :
     */


    /*
    함수명 : searchHashtag
    기능 : 해시태그로 검색
    매개변수 : String keyword
    반환값 : 포스트 목록, 포스트 개수; 포스트 개수는 따로 만들어야.
     */

    @GetMapping("/hashtags")
    public PostListDto searchHashtag(@RequestParam String keyword,
                                     @PageableDefault(size = 20) Pageable pageable){
        return postService.searchByHashtag(keyword, pageable);
    }



    /*
    함수명 : searchUser
    기능 : 사용자 검색
    매개변수 : String keyword
    반환값 : UserBriefDto List,  유저 목록 개수
     */

    @GetMapping("/users")
    public CommunityUseListDto searchUser(@RequestParam String keyword,
                                          @PageableDefault(size = 20) Pageable pageable){
        return communityUserService.searchUser(keyword, pageable);
    }



    /*
    함수명 : searchPost
    기능 : 게시글 검색
    매개변수 : String keyword
    반환값 : PostList, 게시글 개수, boolean hasMore
     */
    @GetMapping("/posts")
    public PostListDto searchPost(@RequestParam String keyword,
                                    @PageableDefault(size = 20) Pageable pageable){
        return postService.searchPost(keyword, pageable);
    }


}
