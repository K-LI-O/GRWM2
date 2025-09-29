package GRWM.backend.service.community;

import GRWM.backend.dto.community.*;
import GRWM.backend.entity.community.*;
import GRWM.backend.entity.user.CommunityUser;
import GRWM.backend.repository.community.CommunityUserHashtagRepository;
import GRWM.backend.repository.community.HashtagRepository;
import GRWM.backend.repository.community.PostHashtagRepository;
import GRWM.backend.repository.community.PostRepository;
import GRWM.backend.repository.user.CommunityUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CommunityUserRepository communityUserRepository;
    private final HashtagRepository hashtagRepository;
    private final PostHashtagRepository postHashtagRepository;
    private final CommunityUserHashtagRepository cuHashtagRepository;

    /*
    함수명 : createPost
    기능 : 커뮤니티 게시글 생성
    매개변수 : PostCreateDto
    반환값: ResponseBody, Long postId

    */

    @Transactional
    public Long createPost(Long communityId, PostCreateDto dto){


        // Post 객체 생성
        Post newPost = new Post(extractOptionalUser(communityId),
                dto.getTitle(),
                dto.getContent().getImages(),
                dto.getContent().getText(),
                dto.getVisibility()
        );
        Post savedPost = postRepository.save(newPost);

        // 해시태그 처리;
        // 이미 존재하는 해시태그인지 확인,

        if(dto.getHashtags() != null) {
            List<PostHashtag> phtagList = new ArrayList<>();
            for (String t : dto.getHashtags()) {
                Hashtag hashtag = hashtagRepository.findByName(t); // DB 조회 1회

                if (hashtag == null) {
                    // 존재하지 않으면 생성 후 저장
                    Hashtag newTag = new Hashtag(t);
                    hashtag = hashtagRepository.save(newTag);

                }

                // 2. PostHashtag 관계가 있는지 확인 (DB 접근 2회)
                // 💡 주의: 이 단계는 '게시글 수정' 시에만 필요하며 '게시글 생성' 시에는 불필요합니다.
                // 게시글 생성(createPost)이라면 무조건 관계가 없으므로 아래 if문은 필요 없습니다.

                // 3. 관계가 없다면 새로 생성하고 저장 (DB 접근 3회)
                PostHashtag postHashtag = new PostHashtag(savedPost, hashtag);
                postHashtagRepository.save(postHashtag);

                 // 해시태그가 있고, 이미 포스트와 관계가 있다면, 안 건들면 됨.

            }
        }

        return savedPost.getId();

    }




    /*
    함수명 : showTimelinePostList
    기능 : 타임라인 리스트 불러오기; 팔로우하는 사람의 최근 포스트
    매개변수 : communityId 추가 필요
    반환값:
    포스트 리스트,
    좋아요 개수 리스트,
    boolean - 더 있는가?
    */

    @Transactional(readOnly = true)
    public List<PostDto> showTimelinePostList(Long communityId){

        // 사용자가 팔로우하는 계정 목록 알아내기

        List<Following> followingList = extractOptionalUser(communityId).getFollowingList();

        // 해당 계정의 포스트 목록 시간순으로 가져오기 향상된 for 문 이용;
        List<CommunityUser> followingUserList = new ArrayList<>();
        for(Following t : followingList){
            followingUserList.add(t.getFollowing());
        }

        // Pageable 객체 생성하여 가져오기
        Pageable pageable = PageRequest.of(0, 40, Sort.by(Sort.Direction.DESC, "createdAt"));
        List<Post> postList = postRepository.findAllByUserIn(followingUserList, pageable);

        // dto 리스트에 담기

        List<PostDto> dtoList = new ArrayList<>();
        for(Post t: postList){

            dtoList.add(postToDto(t));
        }

        // 반환
        return dtoList;
    }


    /*
    함수명 : showPostDetail
    기능 : 게시글 하나의 정보 불러오기
    매개변수 : path variable Long postId
    반환값: Post 정보(유저, 포스트 자체 정보, 리액션), commentList(아이디, 컨텐트, 작성 날짜)

    */
    @Transactional(readOnly = true)
    public PostDto showPostDetail(Long postId){
        // 아이디로 포스트 조회하기
        return  postToDto(findPostById(postId));
    }



    /*
    함수명 : updatePost
    기능 : 게시글 수정하기
    매개변수 : path variable Long postId
    반환값: Dto: 수정된 Post 정보(유저, 포스트 자체 정보, 리액션), commentList(아이디, 컨텐트, 작성 날짜)

    */

    @Transactional
    public PostDto updatePost(Long postId, PostUpdateDto dto){
        // 포스트 불러오기
        Post post = findPostById(postId);

        // 포스트 수정하기
        post.setContent(dto.getContent().getText());
        post.setImageLink(dto.getContent().getImages());
        post.setPostHashtagList(new ArrayList<>());
        post.setVisibility(dto.getVisibility());
        post.setEdited(true);
        post.setPostHashtagList(null);

        // 포스트 저장하기
        Post savedPost = postRepository.save(post);

        // 이미 존재하는 해시태그인지 확인,
        if(dto.getHashtags() != null) {
            List<PostHashtag> phtagList = new ArrayList<>();
            for (String t : dto.getHashtags()) {
                Hashtag hashtag = hashtagRepository.findByName(t); // DB 조회 1회

                if (hashtag == null) {
                    // 존재하지 않으면 생성 후 저장
                    Hashtag newTag = new Hashtag(t);
                    hashtag = hashtagRepository.save(newTag);
                }

                // 2. PostHashtag 관계가 있는지 확인 (DB 접근 2회)
                // 💡 주의: 이 단계는 '게시글 수정' 시에만 필요하며 '게시글 생성' 시에는 불필요합니다.
                // 게시글 생성(createPost)이라면 무조건 관계가 없으므로 아래 if문은 필요 없습니다.
                PostHashtag postHashtag = postHashtagRepository.findByPostAndHashtag(savedPost, hashtag);

                if (postHashtag == null) {
                    // 3. 관계가 없다면 새로 생성하고 저장 (DB 접근 3회)
                    postHashtag = new PostHashtag(savedPost, hashtag);
                    postHashtagRepository.save(postHashtag);
                } // 해시태그가 있고, 이미 포스트와 관계가 있다면, 안 건들면 됨.

            }
        }


        // 반환
        return postToDto(savedPost);

    }


    /*
    함수명 : deletePost
    기능 : 게시글 삭제하기
    매개변수 : path variable Long postId
    반환값: Dto: responseEntity 200 OK

    */

    @Transactional
    public void deletePost(Long postId){
        postRepository.delete(findPostById(postId));
    }


    /*
    함수명 : pinPost
    기능 : 게시글 메인에 고정하기
    매개변수 : path variable Long postId
    반환값: Dto: responseEntity 200 OK

    */


    @Transactional
    public void pinPost(Long communityId, Long postId){
        Post post = findPostById(postId);
        CommunityUser user = extractOptionalUser(communityId);

        user.setPinnedPost(post);
        communityUserRepository.save(user);
    }


    /*
    함수명 : unpinPost
    기능 : 게시글 메인에서 고정 해제하기
    매개변수 : path variable Long postId
    반환값: Dto: responseEntity 200 OK

    */

    @Transactional
    public void unpinPost(Long communityId, Long postId){
        Post post = findPostById(postId);
        CommunityUser user = extractOptionalUser(communityId);
        user.setPinnedPost(null);

        communityUserRepository.save(user);
    }



    /*
    함수명 : getUserPosts
    기능 : 각 계정별 포스트를 리스트로 반환;
    매개변수 : path variable Long postId
    반환값: Dto: responseEntity 200 OK

    */

    @Transactional(readOnly = true)
    public List<PostDto> getUserPosts(Long communityId){
        CommunityUser user = findCommunityUserById(communityId);

        List<Post> postList = postRepository.findByUser(user);

        List<PostDto> dtoList = new ArrayList<>();
        for(Post t: postList){

            dtoList.add(postToDto(t));
        }

        return dtoList;
    }



    // ======== 해시태그 관련 로직 ======= //


         /*
    함수명 : getSubscribedHashtagPostList
    기능  : 사용자가 구독하는 해시태그의 게시글 조회
    매개변수 : Long communityId
    반환값 : List<PostDto>
     */

    @Transactional(readOnly = true)
    public List<PostDto> getSubscribedHashtagPostList(Long communityId){
        // 사용자와 해시태그 관계 elements 불러오기
        List<CommunityUserHashtag> chList =
                cuHashtagRepository.findByUser(findCommunityUserById(communityId));

        // 해시태그 추출
        List<Hashtag> hashtagList = new ArrayList<>();

        for(CommunityUserHashtag t : chList){
            hashtagList.add(t.getHashtag());
        }

        // 해시태그 목록으로 포스트 해시태그 목록 불러와서 포스트 추출;
        List<PostHashtag> phList = postHashtagRepository.findAllByHashtagIn(hashtagList);
        List<Post> postList = new ArrayList<>();
        for(PostHashtag t : phList){
            postList.add(t.getPost());
        }

        List<PostDto> dtoList = new ArrayList<>();
        for (Post t : postList) {
            dtoList.add(postToDto(t));
        }
        return dtoList;
    }





    // ======= 검색 로직 ====== //

        /*
    함수명 : searchHashtag
    기능 : 해시태그로 검색
    매개변수 : String keyword
    반환값 : 포스트 목록, 포스트 개수; 포스트 개수는 따로 만들어야.
     */

    @Transactional(readOnly = true)
    public List<PostDto> searchByHashtag(String keyword){
        // 키워드로 해시태그 찾기
        Hashtag tag;
        try{
            tag = hashtagRepository.findByName(keyword);
        } catch (RuntimeException e) {
            throw new RuntimeException("존재하지 않는 해시태그입니다.");
        }

        // 포스트-해시태그 찾기

        List<PostHashtag> phList = postHashtagRepository.findByHashtag(tag);

        // 포스트 dto 목록 반환
        List<PostDto> dtoList = new ArrayList<>();
        for(PostHashtag t : phList){
            dtoList.add(postToDto(t.getPost()));
        }

        return dtoList;
    }






    /*
    함수명 : searchPost
    기능 : 게시글 검색
    매개변수 : String keyword
    반환값 : PostList, 게시글 개수, boolean hasMore
     */


    @Transactional(readOnly = true)
    public List<PostDto> searchPost(String keyword){
        List<Post> postList = postRepository.findByContentContaining(keyword);

        List<PostDto> dtoList = new ArrayList<>();
        for(Post t : postList){
            dtoList.add(postToDto(t));
        }

        return dtoList;

    }




    // ======= 댓글 반환 로직 ======= //





    // ======= private functions ======== //

    @Transactional(readOnly = true)
    private CommunityUser extractOptionalUser(Long communityId){
        Optional<CommunityUser> optionalUser = communityUserRepository.findById(communityId);
        CommunityUser user = null;


        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }
        else{
            throw new RuntimeException("존재하지 않는 사용자입니다.");
        }

    }





    @Transactional(readOnly = true)
    private CommunityUser findCommunityUserById(Long communityId){

        Optional<CommunityUser> optionalUser = communityUserRepository.findById(communityId);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }
        else{
            throw new RuntimeException("존재하지 않는 사용자입니다.");
        }
    }


    @Transactional(readOnly = true)
    private Post findPostById(Long postId){
        Optional<Post> optionalPost = postRepository.findById(postId);
        Post post = null;
        try {
            if (optionalPost.isPresent()) {
                post = optionalPost.get();
            }
        } catch (RuntimeException e) {
            throw new RuntimeException("존재하지 않는 아이디입니다.");
        }
        return post;
    }

    @Transactional(readOnly = true)
    public PostDto postToDto(Post t){

        CommunityUserBriefDto userDto = new CommunityUserBriefDto(
                t.getUser().getId(),
                t.getUser().getNickname(),
                t.getUser().getProfileImage()
        );



        List<PostHashtag> phList = postHashtagRepository.findByPost(t);
        List<String> hashtagList = new ArrayList<>();
        for(PostHashtag v : phList){
            hashtagList.add(v.getHashtag().getName());
        }

        PostContentDto contentDto = new PostContentDto(t.getContent(), t.getImageLink());

        PostDto postDto = new PostDto(
                t.getId(),
                userDto,
                t.getTitle(),
                contentDto,
                hashtagList,
                t.getVisibility(),
                t.countLikes(),
                t.countComments(),
                t.isEdited(),
                t.getCreatedAt(),
                t.getUpdatedAt()

        );
        return postDto;

    }





}
