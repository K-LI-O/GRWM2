package GRWM.backend.service.community;

import GRWM.backend.dto.community.CommunityUserBriefDto;
import GRWM.backend.entity.community.Liked;
import GRWM.backend.entity.community.Post;
import GRWM.backend.entity.user.CommunityUser;
import GRWM.backend.repository.community.LikedRepository;
import GRWM.backend.repository.community.PostRepository;
import GRWM.backend.repository.user.CommunityUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class LikedService {

    private final LikedRepository likedRepository;
    private final CommunityUserRepository communityUserRepository;
    private final PostRepository postRepository;

    /*
    함수명 : likePost
    기능 : 게시글에 좋아요 표시
    매개변수 : Long postId, Long communityId
    반환값 : responseEntity,
     */

    public void likePost(Long postId, Long communityId){

        Liked liked = new Liked(findPostById(postId), findCommunityUserById(communityId));
        likedRepository.save(liked);
    }



    /*
    함수명 : cancelLike
    기능 : 게시글의 좋아요 취소하기
    매개변수 : Long postId, Long communityId
    반환값 : responseEntity
     */

    public void cancelLike(Long postId, Long communityId){
        // liked 찾아오기
        Liked liked = likedRepository.findByPostAndUser(findPostById(postId), findCommunityUserById(communityId));

        // liked 삭제하기
        likedRepository.delete(liked);
    }



    /*
    함수명 : getPostLikedUserList
    기능 : 게시글에 좋아요를 누른 사용자 리스트 반환
    매개변수 : postId
    반환값 : List<communityUserBriefDto>
     */

    public List<CommunityUserBriefDto> getPostLikedUserList(Long postId){
        List<Liked> likedList = findPostById(postId).getLikedList();

        List<CommunityUserBriefDto> dtoList = new ArrayList<>();
        for(Liked t : likedList){
            dtoList.add(userToDto(t.getUser()));
        }

        return dtoList;
    }





    /*
    함수명 : countLikes
    기능 : 게시글의 좋아요 개수 반환
    매개변수 : Long postId
    반환값 : int countLikes
     */

    public int countLikes(Long postId){
        return findPostById(postId).countLikes();
    }



    // ======= private functions ======= //




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
    public Post findPostById(Long postId){
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

    private CommunityUserBriefDto userToDto(CommunityUser user){

        CommunityUserBriefDto newDto = new CommunityUserBriefDto(
                user.getId(),
                user.getNickname(),
                user.getProfileImage()
        );

        return newDto;

    }

}
