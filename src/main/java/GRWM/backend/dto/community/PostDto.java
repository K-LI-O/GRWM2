package GRWM.backend.dto.community;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {

    private Long postId;

    private CommunityUserBriefDto User;

    //private String title;

    private PostContentDto content;

    private List<String> hashtags;

    private String visibility;

    private int likeCount;
    private int commentCount;

    private boolean isEdited;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
/*
    postId: string,

    content: {
        text?: string,
                images?:string[],
    } //둘 중 하나는 반드시 존재해야 업로드 가능
    Hashtags?: string[],
    visibility: "public" | "friends" | "private", //friends 가 어쩌고
    likeCount: number, //like 따로
    commentCount: number,//commentList 따로
    isEdited: Boolean, //수정됨 표기
    createdAt: Date,
    updatedAt?: Date
*/
}
