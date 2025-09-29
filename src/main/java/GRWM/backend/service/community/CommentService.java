package GRWM.backend.service.community;

import GRWM.backend.dto.community.CommentCreateDto;
import GRWM.backend.dto.community.CommentDto;
import GRWM.backend.dto.community.CommentUpdateDto;
import GRWM.backend.dto.community.CommunityUserBriefDto;
import GRWM.backend.entity.community.Comment;
import GRWM.backend.entity.community.Post;
import GRWM.backend.entity.community.Reply;
import GRWM.backend.entity.user.CommunityUser;
import GRWM.backend.repository.community.CommentRepository;
import GRWM.backend.repository.community.PostRepository;
import GRWM.backend.repository.community.ReplyRepository;
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
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommunityUserRepository communityUserRepository;
    private final PostRepository postRepository;
    private final ReplyRepository replyRepository;





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
    @Transactional
    public CommentDto createComment(Long communityId, Long postId, CommentCreateDto dto){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시물을 찾을 수 없습니다."));

        CommunityUser user = communityUserRepository.findById(communityId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        Comment parentComment = null;
        if(dto.getRootCommentId() != null){
            parentComment = commentRepository.findById(dto.getRootCommentId()).get();
        }
        // 댓글 생성
        Comment comment = new Comment(post, user, parentComment, dto.getContent(), dto.isPrivate());
        Comment savedComment = commentRepository.save(comment);


        // dto에 담아 전달.
        return commentToDto(communityId, savedComment);
    }



    /*
    함수명 : updateComment
    기능 : 댓글을 수정하고, 수정 여부를 표기함
    매개변수 : Long commentId**, String content, boolean isPrivate;
    반환값 : CommentDto
     */

    public CommentDto updateComment(Long communityId, Long commentId, CommentUpdateDto dto){
        // Comment 불러오기
        Comment comment = extractOptionalToComment(commentId);

        // 수정하기
        comment.setContent(dto.getContent());
        comment.setPrivate(dto.isPrivate());
        comment.setEdited(true);

        // 저장하고 리턴하기

        Comment savedComment = commentRepository.save(comment);
        return commentToDto(communityId, savedComment);

    }



    /*
    함수명 : deleteComment
    기능 : 댓글 삭제
    매개변수 : commentId
    반환값 : void
     */

    public void deleteComment(Long commentId){
        commentRepository.delete(extractOptionalToComment(commentId));
    }



    /*
    함수명 : getCommentList
    기능 : 한 게시글의 댓글 리스트 반환
    매개변수 : postId
    반환값 : List<CommentDto>, 댓글 개수
     */


    public List<CommentDto> getCommentList(Long communityId, Long postId){
        List<Comment> commentList = findPostById(postId).getCommentList();

        List<CommentDto> dtoList = new ArrayList<>();
        for(Comment t : commentList){
            dtoList.add(commentToDto(communityId, t));
        }

        return dtoList;
    }





    /*
    함수명 : getCommentCount
    기능 :  한 게시글의 댓글 개수 반환
    매개변수 : postId
    반환값 : int commentCount;
     */

    public int getCommentCount(Long postId){
        return findPostById(postId).getCommentList().size();
    }




    // ======= private functions ======= //

    private Comment extractOptionalToComment(Long commentId){

        Optional<Comment> optionalComment = commentRepository.findById(commentId);

        if(optionalComment.isPresent()){
            return optionalComment.get();
        } else{
            throw new RuntimeException("존재하지 않는 댓글입니다.");
        }
    }



    private CommentDto commentToDto(Long communityId, Comment comment){

        Long parentComment = null;
        if(comment.getParentComment() != null){
            parentComment = comment.getParentComment().getId();
        }

        CommentDto newDto = new CommentDto(
                comment.getId(),
                userToDto(extractOptionalUser(communityId)),
                comment.getContent(),
                comment.isPrivate(),
                parentComment,
                comment.isEdited()
        );

        return newDto;

    }





    private CommunityUser extractOptionalUser(Long communityId){
        Optional<CommunityUser> optionalUser = communityUserRepository.findById(communityId);
        CommunityUser user = null;


        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else{
            throw new RuntimeException("존재하지 않는 사용자입니다.");
        }
    }





    private CommunityUserBriefDto userToDto(CommunityUser user){

        CommunityUserBriefDto newDto = new CommunityUserBriefDto(
                user.getId(),
                user.getNickname(),
                user.getProfileImage()
        );

        return newDto;

    }



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

}
