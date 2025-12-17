package wsd.community.domain.post.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import wsd.community.domain.post.entity.PostType;

public record PostCreateRequest(
        @NotBlank(message = "제목은 필수입니다.") String title,

        @NotBlank(message = "내용은 필수입니다.") String content,

        @NotNull(message = "게시글 타입은 필수입니다.") PostType type) {
}
