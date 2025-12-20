package wsd.community.domain.comment.response;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import wsd.community.domain.comment.entity.Comment;

public record CommentResponse(
        Long id,
        String content,
        String writerName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long likeCount,
        boolean isHidden) {

    @QueryProjection
    public CommentResponse {
    }

    public static CommentResponse from(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getUser().getName(),
                comment.getCreatedAt(),
                comment.getUpdatedAt(),
                comment.getLikeCount(),
                comment.isHidden());
    }
}
