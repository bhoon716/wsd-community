package wsd.community.domain.post.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import wsd.community.domain.post.entity.PostType;

@Schema(description = "게시글 생성 요청 DTO")
public record PostCreateRequest(
                @NotBlank(message = "제목은 필수입니다.") @Schema(description = "게시글 제목", example = "게시글 제목입니다.") String title,

                @NotBlank(message = "내용은 필수입니다.") @Schema(description = "게시글 내용", example = "게시글 내용입니다.") String content,

                @NotNull(message = "게시글 타입은 필수입니다.") @Schema(description = "게시글 타입 (GENERAL, QNA)", example = "GENERAL") PostType type) {
}
