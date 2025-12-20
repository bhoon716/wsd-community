package wsd.community.domain.report.response;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import lombok.Builder;
import wsd.community.domain.report.entity.Report;
import wsd.community.domain.report.entity.ReportAction;
import wsd.community.domain.report.entity.ReportStatus;
import wsd.community.domain.report.entity.ReportType;
import com.querydsl.core.annotations.QueryProjection;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "신고 상세 응답 DTO")
public record ReportDetailResponse(
        @Schema(description = "신고 ID", example = "1") Long id,
        @Schema(description = "신고자 ID", example = "10") Long reporterId,
        @Schema(description = "신고자 이메일", example = "reporter@example.com") String reporterEmail,
        @Schema(description = "신고 대상 ID", example = "100") Long targetId,
        @Schema(description = "신고 유형", example = "POST") ReportType type,
        @Schema(description = "신고 사유", example = "부적절한 게시글") String reason,
        @Schema(description = "신고 상세 내용", example = "욕설 포함") String description,
        @Schema(description = "대상 제목", example = "문제의 게시글") String targetTitle,
        @Schema(description = "대상 내용", example = "문제의 내용") String targetContent,
        @Schema(description = "처리 사유", example = "확인 후 차단 조치") String resolvedReason,
        @Schema(description = "처리자 이메일", example = "admin@example.com") String resolvedByEmail,
        @Schema(description = "처리 일시") LocalDateTime resolvedAt,
        @Schema(description = "조치 내용", example = "BAN_USER") ReportAction action,
        @Schema(description = "신고 상태", example = "RESOLVED") ReportStatus status,
        @Schema(description = "신고 일시") LocalDateTime reportedAt) {

    @QueryProjection
    public ReportDetailResponse {
    }

    public static ReportDetailResponse from(Report report) {
        return ReportDetailResponse.builder()
                .id(report.getId())
                .reporterId(report.getReporter().getId())
                .reporterEmail(report.getReporter().getEmail())
                .targetId(report.getTargetId())
                .type(report.getType())
                .reason(report.getReason())
                .description(report.getDescription())
                .targetTitle(report.getTargetTitle())
                .targetContent(report.getTargetContent())
                .resolvedReason(report.getResolvedReason())
                .resolvedByEmail(report.getResolvedBy() != null ? report.getResolvedBy().getEmail() : null)
                .resolvedAt(report.getResolvedAt())
                .action(report.getAction())
                .status(report.getStatus())
                .reportedAt(report.getCreatedAt())
                .build();
    }
}
