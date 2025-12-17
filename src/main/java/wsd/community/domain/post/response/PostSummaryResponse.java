package wsd.community.domain.post.response;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import wsd.community.domain.post.entity.PostType;

public record PostSummaryResponse(
        Long id,
        String title,
        PostType type,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String writerName,
        Long likeCount) {
    @QueryProjection
    public PostSummaryResponse {
    }
}
