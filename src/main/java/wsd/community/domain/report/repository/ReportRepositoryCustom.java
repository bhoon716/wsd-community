package wsd.community.domain.report.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import wsd.community.domain.report.entity.ReportStatus;
import wsd.community.domain.report.entity.ReportType;
import wsd.community.domain.report.response.ReportSummaryResponse;
import java.util.Optional;
import wsd.community.domain.report.response.ReportDetailResponse;
import wsd.community.domain.user.entity.User;

public interface ReportRepositoryCustom {

    Page<ReportSummaryResponse> getReports(ReportStatus status, ReportType type, Pageable pageable);

    Page<ReportSummaryResponse> getMyReports(User reporter, Pageable pageable);

    Optional<ReportDetailResponse> getReportDetail(Long reportId);
}
