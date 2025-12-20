package wsd.community.domain.report.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import wsd.community.domain.report.entity.ReportType;

@Schema(description = "신고 생성 요청 DTO")
public record ReportCreateRequest(
        @NotNull(message = "신고 대상 ID는 필수입니다.") @Schema(description = "신고 대상 ID", example = "1") Long targetId,
        @NotNull(message = "신고 유형은 필수입니다.") @Schema(description = "신고 유형", example = "E_POST") ReportType type,
        @NotBlank(message = "신고 사유는 필수입니다.") @Size(max = 50, message = "신고 사유는 50자 이하여야 합니다.") @Schema(description = "신고 사유", example = "부적절한 게시글입니다.") String reason,
        @Size(max = 500, message = "신고 상세 내용은 500자 이하여야 합니다.") @Schema(description = "신고 상세 내용", example = "게시글 내용에 욕설이 포함되어 있습니다.") String description) {
}
