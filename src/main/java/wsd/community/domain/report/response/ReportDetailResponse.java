package wsd.community.domain.report.response;

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
public record ReportDetailResponse(
        Long id,
        Long reporterId,
        String reporterEmail,
        Long targetId,
        ReportType type,
        String reason,
        String description,
        String targetTitle,
        String targetContent,
        String resolvedReason,
        String resolvedByEmail,
        LocalDateTime resolvedAt,
        ReportAction action,
        ReportStatus status,
        LocalDateTime reportedAt) {

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
