package wsd.community.domain.post.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import wsd.community.domain.comment.response.CommentResponse;
import wsd.community.domain.post.entity.Post;
import wsd.community.domain.post.entity.PostType;

@Schema(description = "게시글 상세 응답 DTO")
public record PostDetailResponse(
        @Schema(description = "게시글 ID", example = "1") Long id,
        @Schema(description = "제목", example = "게시글 제목") String title,
        @Schema(description = "내용", example = "게시글 내용") String content,
        @Schema(description = "게시글 타입", example = "GENERAL") PostType type,
        @Schema(description = "작성일시") LocalDateTime createdAt,
        @Schema(description = "수정일시") LocalDateTime updatedAt,
        @Schema(description = "작성자 이름", example = "홍길동") String writerName,
        @Schema(description = "좋아요 수", example = "10") Long likeCount,
        @Schema(description = "숨김 여부", example = "false") boolean isHidden,
        @Schema(description = "댓글 목록") List<CommentResponse> comments) {
    public static PostDetailResponse from(Post post, List<CommentResponse> comments) {
        return new PostDetailResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getType(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                post.getUser().getName(),
                post.getLikeCount(),
                post.isHidden(),
                comments);
    }
}
