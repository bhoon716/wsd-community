package wsd.community.domain.report.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import wsd.community.domain.report.entity.ReportAction;

@Schema(description = "신고 처리 요청 DTO (관리자 전용)")
public record ReportProcessRequest(
                @NotNull(message = "조치 내용을 선택해주세요.") @Schema(description = "조치 내용", example = "BAN_USER") ReportAction action,
                @Size(max = 500, message = "처리 사유는 500자 이하여야 합니다.") @Schema(description = "처리 사유", example = "부적절한 게시글 작성으로 인한 사용자 차단") String reason) {
}
