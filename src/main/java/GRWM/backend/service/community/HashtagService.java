package GRWM.backend.service.community;

import GRWM.backend.entity.community.CommunityUserHashtag;
import GRWM.backend.entity.community.Hashtag;

import GRWM.backend.entity.user.CommunityUser;
import GRWM.backend.repository.community.CommunityUserHashtagRepository;
import GRWM.backend.repository.community.HashtagRepository;
import GRWM.backend.repository.community.PostHashtagRepository;
import GRWM.backend.repository.community.PostRepository;
import GRWM.backend.repository.user.CommunityUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HashtagService {

    private final HashtagRepository hashtagRepository;
    private final CommunityUserHashtagRepository cuHashtagRepository;
    private final CommunityUserRepository communityUserRepository;


    /*
    함수명 : createHashtag
    기능  : 해시태그 생성
    매개변수 : String hashtag
    반환값 : responseEntity
     */

    public Long createHashtag(String hashtag){
        Hashtag newHashtag = new Hashtag(hashtag);

        return hashtagRepository.save(newHashtag).getId();
    }

    /*
    함수명 : getSubscribedHashtagList
    기능  : 구독한 해시태그 리스트 불러오기
    매개변수 : Long communityId
    반환값 : List<String>
     */ // 구독한 해시태그 리스트와 해시태그 개수는 따로 만들어야.

    public List<String> getSubscribedHashtagList(Long communityId){
        // 사용자와 해시태그 관계 elements 불러오기
        List<CommunityUserHashtag> chList = cuHashtagRepository.findByUser(findCommunityUserById(communityId));

        // 해시태그 추출;
        List<String> hashtagList = new ArrayList<>();
        for(CommunityUserHashtag t : chList){
            hashtagList.add("#"+ t.getHashtag().getName());
        }

        return hashtagList;
    }



        /*
    함수명 : subscribeHashtag
    기능  : 해시태그 구독하기
    매개변수 : Long communityId, tagId
    반환값 : ResponseEntity
     */

    public void subscribeHashtag(Long communityId, Long tagId){
        CommunityUser user = findCommunityUserById(communityId);
        Hashtag tag = findHashtagById(tagId);

        CommunityUserHashtag ch = new CommunityUserHashtag(user, tag);
        cuHashtagRepository.save(ch);
    }


    /*
    함수명 : unsubscribeHashtag
    기능  : 해시태그 구독 해제
    매개변수 : Long communityId, Long tagId
    반환값 : ResponseEntity
     */

    public void unsubscribeHashtag(Long communityId, Long tagId){
        CommunityUser user = findCommunityUserById(communityId);
        Hashtag tag = findHashtagById(tagId);

        CommunityUserHashtag ch = cuHashtagRepository.findByUserAndHashtag(user, tag);
        cuHashtagRepository.delete(ch);
    }



    /*
    함수명 : updateHashtagOrder
    기능  : 사용자에게 보이는 해시태그 목록의 순서 변경
    매개변수 : 해시태그 목록
    반환값 : 해시태그 목록
     */





            /*
    함수명 : getSubscribedHashtagCount
    기능  : 구독한 해시태그 리스트 개수 불러오기
    매개변수 : Long communityId
    반환값 : int
     */

    public int getSubscribedHashtagCount(Long communityId){
        CommunityUser user = findCommunityUserById(communityId);
        return user.getCuHashtagList().size();
    }


    /*
    함수명 : getHashtagId
    기능  : 해시태그 아이디를 반환한다.
    매개변수 : String keyword
    반환값 : Long id
     */
    public Long getHashtagId(String keyword){
        return hashtagRepository.findByName(keyword).getId();
    }







    // ======= private logics ======= //


    private CommunityUser findCommunityUserById(Long communityId){

        Optional<CommunityUser> optionalUser = communityUserRepository.findById(communityId);
        CommunityUser user = null;
        try {
            if (optionalUser.isPresent()) {
                user = optionalUser.get();
            }
        } catch (RuntimeException e) {
            throw new RuntimeException("존재하지 않는 사용자입니다.");
        }
        return user;
    }

    private Hashtag findHashtagById(Long tagId){

        Optional<Hashtag> optionalHashtag = hashtagRepository.findById(tagId);
        Hashtag tag = null;
        try {
            if (optionalHashtag.isPresent()) {
                tag = optionalHashtag.get();
            }
        } catch (RuntimeException e) {
            throw new RuntimeException("존재하지 않는 사용자입니다.");
        }
        return tag;
    }


}
