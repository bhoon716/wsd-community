package wsd.community.domain.report.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import wsd.community.domain.report.entity.ReportStatus;
import wsd.community.domain.report.entity.ReportType;
import wsd.community.domain.report.response.ReportResponse;

public interface ReportRepositoryCustom {

    Page<ReportResponse> getReports(ReportStatus status, ReportType type, Pageable pageable);
}
