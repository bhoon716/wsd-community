package wsd.community.domain.comment.response;

import io.swagger.v3.oas.annotations.media.Schema;
import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import wsd.community.domain.comment.entity.Comment;

@Schema(description = "댓글 응답 DTO")
public record CommentResponse(
        @Schema(description = "댓글 ID", example = "1") Long id,
        @Schema(description = "내용", example = "댓글 내용") String content,
        @Schema(description = "작성자 이름", example = "홍길동") String writerName,
        @Schema(description = "작성일시") LocalDateTime createdAt,
        @Schema(description = "수정일시") LocalDateTime updatedAt,
        @Schema(description = "좋아요 수", example = "5") Long likeCount,
        @Schema(description = "숨김 여부", example = "false") boolean isHidden) {

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
