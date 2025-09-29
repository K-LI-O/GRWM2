package GRWM.backend.controller.community;

import GRWM.backend.dto.community.CommunityUserBriefDto;
import GRWM.backend.dto.community.PostDto;
import GRWM.backend.service.community.CommunityUserService;
import GRWM.backend.service.community.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public List<PostDto> searchHashtag(@RequestParam String keyword){
        return postService.searchByHashtag(keyword);
    }



    /*
    함수명 : searchUser
    기능 : 사용자 검색
    매개변수 : String keyword
    반환값 : UserBriefDto List,  유저 목록 개수
     */

    @GetMapping("/users")
    public List<CommunityUserBriefDto> searchUser(@RequestParam String keyword){
        return communityUserService.searchUser(keyword);
    }



    /*
    함수명 : searchPost
    기능 : 게시글 검색
    매개변수 : String keyword
    반환값 : PostList, 게시글 개수, boolean hasMore
     */
    @GetMapping("/posts")
    public List<PostDto> searchPost(@RequestParam String keyword){
        return postService.searchPost(keyword);
    }


}
