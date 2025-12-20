package wsd.community.domain.post.response;

import io.swagger.v3.oas.annotations.media.Schema;
import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import wsd.community.domain.post.entity.PostType;

@Schema(description = "게시글 요약 응답 DTO")
public record PostSummaryResponse(
        @Schema(description = "게시글 ID", example = "1") Long id,
        @Schema(description = "제목", example = "게시글 제목") String title,
        @Schema(description = "게시글 타입", example = "GENERAL") PostType type,
        @Schema(description = "작성일시") LocalDateTime createdAt,
        @Schema(description = "수정일시") LocalDateTime updatedAt,
        @Schema(description = "작성자 이름", example = "홍길동") String writerName,
        @Schema(description = "좋아요 수", example = "10") Long likeCount,
        @Schema(description = "숨김 여부", example = "false") boolean isHidden,
        @Schema(description = "고정 여부", example = "false") boolean isPinned) {
    @QueryProjection
    public PostSummaryResponse {
    }
}
