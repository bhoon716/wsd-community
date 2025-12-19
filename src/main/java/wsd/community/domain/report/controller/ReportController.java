package wsd.community.domain.report.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import wsd.community.domain.report.entity.ReportAction;
import java.util.Map;

import jakarta.validation.Valid;
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
import wsd.community.domain.report.request.ReportUpdateRequest;
import wsd.community.domain.report.response.ReportResponse;
import wsd.community.domain.report.service.ReportService;
import wsd.community.security.auth.CustomUserDetails;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/{reportId}")
    public ResponseEntity<ReportResponse> getReport(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long reportId) {
        return ResponseEntity.ok(reportService.getReport(reportId, userDetails.getUser()));
    }

    @GetMapping("/me")
    public ResponseEntity<Page<ReportResponse>> getMyReports(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(reportService.getMyReports(userDetails.getUser(), pageable));
    }

    @PostMapping
    public ResponseEntity<ReportResponse> createReport(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody ReportCreateRequest request) {
        return ResponseEntity.ok(reportService.createReport(userDetails.getUser(), request));
    }

    @PutMapping("/{reportId}")
    public ResponseEntity<ReportResponse> updateReport(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long reportId,
            @Valid @RequestBody ReportUpdateRequest request) {
        return ResponseEntity.ok(reportService.updateReport(reportId, userDetails.getUser(), request));
    }

    @DeleteMapping("/{reportId}")
    public ResponseEntity<Void> deleteReport(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long reportId) {
        reportService.deleteReport(reportId, userDetails.getUser());
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ReportResponse>> getReports(
            @RequestParam(required = false) ReportStatus status,
            @RequestParam(required = false) ReportType type,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(reportService.getReports(status, type, pageable));
    }

    @PostMapping("/{reportId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReportResponse> processReport(
            @PathVariable Long reportId,
            @RequestBody Map<String, String> request) {

        String actionStr = request.get("action");
        ReportAction action = ReportAction.valueOf(actionStr.toUpperCase());

        return ResponseEntity.ok(reportService.processReport(reportId, action));
    }
}
