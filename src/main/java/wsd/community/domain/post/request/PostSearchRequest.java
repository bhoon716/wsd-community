package wsd.community.domain.post.request;

import io.swagger.v3.oas.annotations.media.Schema;
import wsd.community.domain.post.entity.PostType;

@Schema(description = "게시글 검색 요청 DTO")
public record PostSearchRequest(
        @Schema(description = "게시글 타입 (GENERAL, QNA)", example = "GENERAL") PostType type,
        @Schema(description = "검색 키워드", example = "검색어") String keyword) {
}
