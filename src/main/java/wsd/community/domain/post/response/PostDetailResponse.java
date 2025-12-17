package wsd.community.domain.post.response;

import java.time.LocalDateTime;
import wsd.community.domain.post.entity.Post;
import wsd.community.domain.post.entity.PostType;

public record PostDetailResponse(
        Long id,
        String title,
        String content,
        PostType type,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String writerName,
        Long likeCount) {
    public static PostDetailResponse from(Post post) {
        return new PostDetailResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getType(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                post.getUser().getName(),
                post.getLikeCount());
    }
}
