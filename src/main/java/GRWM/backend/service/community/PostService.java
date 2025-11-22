package GRWM.backend.service.community;

import GRWM.backend.dto.community.*;
import GRWM.backend.entity.community.*;
import GRWM.backend.entity.user.CommunityUser;
import GRWM.backend.repository.community.*;
import GRWM.backend.repository.user.CommunityUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.nio.file.AccessDeniedException;
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
    private final FollowingRepository followingRepository;
    private final BlockListRepository blockListRepository;



    // ======== CRUD 로직 ======== ///
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
                dto.getContent().getImages(),
                dto.getContent().getText(),
                dto.getVisibility()
        );
        Post savedPost = postRepository.save(newPost);
        PostDto savedDto = postToDto(savedPost);

        // 해시태그 처리(private 함수로 따로 작성)
        saveHashtag(savedPost, dto.getHashtags());

        return savedPost.getId();

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
        post.setVisibility(dto.getVisibility());
        post.setEdited(true);
        List<PostHashtag> existingHashtags = post.getPostHashtagList();
        existingHashtags.clear();
        postHashtagRepository.saveAll(existingHashtags);

        // 포스트 저장하기
        Post savedPost = postRepository.save(post);


        saveHashtag(savedPost, dto.getHashtags());
        // 이미 존재하는 해시태그인지 확인,

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


    // ======= Pageable 사용 로직 ======= //



    /*
    함수명 : showTimelinePostList
    기능 : 타임라인 리스트 불러오기; 팔로우하는 사람의 최근 포스트
    매개변수 : communityId 추가 필요
    반환값: PostListDto
    포스트 리스트,
    boolean - hasMore
    */

    @Transactional(readOnly = true)
    public PostListDto showTimelinePostList(Long communityId, Pageable pageable) throws AccessDeniedException{

        // 사용자가 팔로우하는 계정 목록 알아내기

        List<Following> followingList = extractOptionalUser(communityId).getFollowingList();

        // 해당 계정의 포스트 목록 시간순으로 가져오기 향상된 for 문 이용;
        List<Long> followingUserList = new ArrayList<>();
        followingUserList.add(extractOptionalUser(communityId).getId());
        for(Following t : followingList){
            followingUserList.add(t.getFollowing().getId());
        }

        // Pageable 객체 추가 설정
        Pageable p = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createdAt")
        );
        Slice<Post> postSlice = postRepository.findTimelinePostsWithVisibility(communityId, followingUserList, p);
        List<Post> postList = postSlice.getContent();

        // dto 리스트에 담기
        List<PostDto> dtoList = new ArrayList<>();
        for(Post t: postList){
            if(checkVisibility(t.getId(), communityId)) {
                dtoList.add(postToDto(t));
            }
        }

        // 반환
        return new PostListDto(dtoList, postSlice.hasNext());
    }


        /*
    함수명 : getUserPosts
    기능 : 각 계정별 포스트를 리스트로 반환;
    매개변수 : path variable Long postId
    반환값: Dto: responseEntity 200 OK

    */

    @Transactional(readOnly = true)
    public PostListDto getUserPosts(Long targetId, Pageable pageable, Long readerId) throws AccessDeniedException{
        CommunityUser user = findCommunityUserById(targetId); // 접속자
        // 계정 소유자 객체
        // 관계 반환



        // pageable 객체 생성;
        Pageable p = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        Slice<Post> postSlice = postRepository.findUserPagePostsWithVisibility(readerId, targetId, p);
        List<Post> postList = postSlice.getContent();

        List<PostDto> dtoList = new ArrayList<>();
        for(Post t: postList){

            if(checkVisibility(t.getId(), readerId)){
            dtoList.add(postToDto(t));
            }
        }

        return new PostListDto(dtoList, postSlice.hasNext());
    }



   // ======= 프로필 메인 게시물 설정 로직 ======= ///


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







    // ======== 해시태그 관련 게시물 조회 로직 ======= //


         /*
    함수명 : getSubscribedHashtagPostList
    기능  : 사용자가 구독하는 해시태그의 게시글 조회
    매개변수 : Long communityId
    반환값 : List<PostDto>
     */

    @Transactional(readOnly = true)
    public List<PostDto> getSubscribedHashtagPostList(Long communityId, Pageable pageable){

        // pageable 객체 생성;
        Pageable p = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        // 사용자와 해시태그 관계 elements 불러오기
        List<CommunityUserHashtag> chList =
                cuHashtagRepository.findByUser(findCommunityUserById(communityId));

        // 해시태그 추출
        List<Long> hashtagIds = new ArrayList<>();
        for(CommunityUserHashtag t : chList){
            hashtagIds.add(t.getHashtag().getId());
        }

        // 해시태그 목록으로 포스트 불러오기, 그들 중 public과 private는 포함.
        // 해시태그 목록으로 포스트 해시태그 목록 불러와서 포스트 추출;
        Slice<Post> postSlice = postRepository.findPostsByHashtagAndVisibility(hashtagIds, communityId, p);
        List<Post> postList = postSlice.getContent();

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
    public PostListDto searchByHashtag(String keyword, Pageable pageable, Long readerId) throws Exception {
        // 키워드로 해시태그 찾기
        Hashtag tag;
        try{
            tag = hashtagRepository.findByName(keyword);
        } catch (RuntimeException e) {
            throw new RuntimeException("존재하지 않는 해시태그입니다.");
        }

        // Pageable 객체 생성
        Pageable p = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        // 포스트 찾기
        Slice<Post> postSlice = postRepository.searchPostsByHashtagAndVisibility(tag.getId(), readerId, p);
        List<Post> postList = postSlice.getContent();

        // 포스트 dto 목록 반환
        List<PostDto> dtoList = new ArrayList<>();
        if (postList.isEmpty()) {
            return new PostListDto(dtoList, false);
        }

        for(Post t : postList){
            dtoList.add(postToDto(t));

        }

        return new PostListDto(dtoList, postSlice.hasNext());
    }






    /*
    함수명 : searchPost
    기능 : 게시글 검색
    매개변수 : String keyword
    반환값 : PostList, 게시글 개수, boolean hasMore
     */


    @Transactional(readOnly = true)
    public PostListDto searchPost(String keyword, Pageable pageable, Long readerId) throws AccessDeniedException {
        // Pageable 객체 생성
        Pageable p = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        // 포스트 목록 슬라이스로 찾아오기
        Slice<Post> postSlice = postRepository.findSearchPostsWithVisibility(keyword, readerId, pageable);
        List<Post> postList = postSlice.getContent();

        List<PostDto> dtoList = new ArrayList<>();
        for(Post t : postList){
            if(checkVisibility(t.getId(), readerId)) {
                dtoList.add(postToDto(t));
            }
        }

        return new PostListDto(dtoList, postSlice.hasNext());
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



    private String getRelationshipOnPost(CommunityUser user, CommunityUser target) {
        if(user.equals(target)) return "private"; // 모든 것 볼 수 있음
        else if (followingRepository.findByFollowingAndFollower(user, target) != null
                && followingRepository.findByFollowingAndFollower(target, user) != null)
            return "friends"; // public 과 friends만 볼 수 있음
        else return "public";

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

    private void saveHashtag(Post savedPost, List<String> hashtags){


        // 이미 존재하는 해시태그인지 확인,
        if(hashtags != null) {
            List<PostHashtag> phtagList = new ArrayList<>();
            for (String t : hashtags) {
                Hashtag hashtag = hashtagRepository.findByName(t); // DB 조회 1회

                if (hashtag == null) {
                    // 존재하지 않으면 생성 후 저장
                    Hashtag newTag = new Hashtag(t);
                    hashtag = hashtagRepository.save(newTag);
                }

                // 2. PostHashtag 관계가 있는지 확인 (DB 접근 2회)
                PostHashtag postHashtag = postHashtagRepository.findByPostAndHashtag(savedPost, hashtag);

                if (postHashtag == null) {
                    // 3. 관계가 없다면 새로 생성하고 저장 (DB 접근 3회)
                    postHashtag = new PostHashtag(savedPost, hashtag);
                    postHashtagRepository.save(postHashtag);
                } // 해시태그가 있고, 이미 포스트와 관계가 있다면, 안 건들면 됨.

            }
        }
    }

    public boolean checkVisibility(Long postId, Long readerId) throws AccessDeniedException {
        Post post = findPostById(postId);
        if (post == null) {
            // 알 수 없습니다: 포스트가 존재하지 않으므로, 가시성 체크를 할 수 없습니다.
            // false를 반환하거나, 적절한 예외를 던지는 것이 일반적입니다.
            return false; // 혹은 throw new PostNotFoundException("포스트를 찾을 수 없습니다.");
        }

        if (post.getUser().getId().equals(readerId)) {
            return true;
        }

        // 2. 전체 공개인지 확인
        if ("public".equals(post.getVisibility())) {
            return true;
        }

        // 3. 친구 공개인지 확인
        if ("friends".equals(post.getVisibility())) {
            return isUsersFriend(post.getUser().getId(), readerId);

        }

        // 4. 나만 보기(private)이거나 그 외 조건 불만족 시 거부
        // "private" 포스트는 1번(본인 확인)에서 이미 처리되지 않았으므로 여기서 거부됩니다.
        return false;
    }

    // 친구 관계를 확인하는 간단한 로직 (FriendshipRepository 사용 가정)
    private boolean isUsersFriend(Long creatorId, Long readerId) {

        if(followingRepository.existsByFollowingAndFollower(
                findCommunityUserById(creatorId),
                findCommunityUserById(readerId)
        ) && followingRepository.existsByFollowingAndFollower(
                findCommunityUserById(readerId),
                findCommunityUserById(creatorId)
        )) return true;
        else return false;
        }

    }



