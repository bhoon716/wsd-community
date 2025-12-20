package wsd.community.domain.report.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "신고 수정 요청 DTO")
public record ReportUpdateRequest(
                @NotBlank(message = "신고 사유는 필수입니다.") @Size(max = 50, message = "신고 사유는 50자 이하여야 합니다.") @Schema(description = "신고 사유", example = "수정된 신고 사유입니다.") String reason,
                @NotBlank(message = "신고 내용은 필수입니다.") @Size(max = 500, message = "신고 내용은 500자 이하여야 합니다.") @Schema(description = "신고 상세 내용", example = "수정된 신고 상세 내용입니다.") String description) {
}
