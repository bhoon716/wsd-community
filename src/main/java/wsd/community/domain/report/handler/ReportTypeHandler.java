package wsd.community.domain.report.handler;

import java.util.List;
import wsd.community.domain.report.entity.Report;
import wsd.community.domain.report.entity.ReportType;
import wsd.community.domain.report.request.ReportCreateRequest;
import wsd.community.domain.user.entity.User;

public interface ReportTypeHandler {

    ReportType getType();

    Report createReport(User reporter, ReportCreateRequest request);

    void hideTarget(Report report);

    void deleteTarget(Report report);

    List<Report> getRelatedPendingReports(Report report);
}
