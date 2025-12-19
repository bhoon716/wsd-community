package wsd.community.domain.report.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import wsd.community.common.response.CommonResponse;

import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wsd.community.domain.report.entity.ReportStatus;
import wsd.community.domain.report.entity.ReportType;
import wsd.community.domain.report.request.ReportCreateRequest;
import wsd.community.domain.report.request.ReportProcessRequest;
import wsd.community.domain.report.request.ReportUpdateRequest;
import wsd.community.domain.report.response.ReportDetailResponse;
import wsd.community.domain.report.response.ReportSummaryResponse;
import wsd.community.domain.report.service.ReportService;
import wsd.community.security.auth.CustomUserDetails;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/{reportId}")
    public ResponseEntity<CommonResponse<ReportDetailResponse>> getReport(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long reportId) {
        return CommonResponse.ok(reportService.getReport(reportId, userDetails.getUser()), "신고 상세 조회 성공");
    }

    @GetMapping("/me")
    public ResponseEntity<CommonResponse<Page<ReportSummaryResponse>>> getMyReports(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return CommonResponse.ok(
                reportService.getMyReports(userDetails.getUser(), pageable),
                "내 신고 목록 조회 성공");
    }

    @PostMapping
    public ResponseEntity<CommonResponse<ReportDetailResponse>> createReport(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody ReportCreateRequest request) {
        ReportDetailResponse response = reportService.createReport(userDetails.getUser(), request);
        return CommonResponse.created(response, URI.create("/api/reports/" + response.id()), "신고 생성 성공");
    }

    @PutMapping("/{reportId}")
    public ResponseEntity<CommonResponse<ReportDetailResponse>> updateReport(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long reportId,
            @Valid @RequestBody ReportUpdateRequest request) {
        return CommonResponse.ok(reportService.updateReport(reportId, userDetails.getUser(), request), "신고 수정 성공");
    }

    @DeleteMapping("/{reportId}")
    public ResponseEntity<CommonResponse<Void>> cancelReport(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long reportId) {
        reportService.cancelReport(reportId, userDetails.getUser());
        return CommonResponse.noContent("신고 취소 성공");
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommonResponse<Page<ReportSummaryResponse>>> getReports(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(required = false) ReportStatus status,
            @RequestParam(required = false) ReportType type,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return CommonResponse.ok(reportService.getReports(userDetails.getUser(), status, type, pageable),
                "신고 목록 조회 성공");
    }

    @PostMapping("/{reportId}/process")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommonResponse<ReportDetailResponse>> processReport(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long reportId,
            @Valid @RequestBody ReportProcessRequest request) {

        return CommonResponse.ok(
                reportService.processReport(reportId, request.action(), request.reason(), userDetails.getUser()),
                "신고 처리 성공");
    }
}
