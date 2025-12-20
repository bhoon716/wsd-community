package wsd.community.domain.report.response;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import lombok.Builder;
import wsd.community.domain.report.entity.Report;
import wsd.community.domain.report.entity.ReportStatus;
import wsd.community.domain.report.entity.ReportType;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "신고 요약 응답 DTO")
public record ReportSummaryResponse(
        @Schema(description = "신고 ID", example = "1") Long id,
        @Schema(description = "신고자 ID", example = "10") Long reporterId,
        @Schema(description = "신고자 이메일", example = "reporter@example.com") String reporterEmail,
        @Schema(description = "신고 대상 ID", example = "100") Long targetId,
        @Schema(description = "신고 유형", example = "POST") ReportType type,
        @Schema(description = "신고 사유", example = "부적절한 게시글") String reason,
        @Schema(description = "대상 제목", example = "문제의 게시글") String targetTitle,
        @Schema(description = "신고 상태", example = "PENDING") ReportStatus status,
        @Schema(description = "신고 일시") LocalDateTime reportedAt) {

    @QueryProjection
    public ReportSummaryResponse {
    }

    public static ReportSummaryResponse from(Report report) {
        return ReportSummaryResponse.builder()
                .id(report.getId())
                .reporterId(report.getReporter().getId())
                .reporterEmail(report.getReporter().getEmail())
                .targetId(report.getTargetId())
                .type(report.getType())
                .reason(report.getReason())
                .targetTitle(report.getTargetTitle())
                .status(report.getStatus())
                .reportedAt(report.getCreatedAt())
                .build();
    }
}
