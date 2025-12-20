package wsd.community.domain.comment.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "댓글 생성 요청 DTO")
public record CommentCreateRequest(
                @NotBlank(message = "댓글 내용은 필수입니다.") @Size(max = 1000, message = "댓글은 1000자 이하로 작성해주세요.") @Schema(description = "댓글 내용", example = "댓글 내용입니다.") String content) {
}
