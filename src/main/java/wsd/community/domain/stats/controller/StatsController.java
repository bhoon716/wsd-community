package wsd.community.domain.stats.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wsd.community.common.response.CommonResponse;
import wsd.community.domain.stats.response.TopCommenterResponse;
import wsd.community.domain.stats.response.TopWriterResponse;
import wsd.community.domain.stats.service.StatsService;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    @GetMapping("/users/top-writers")
    public ResponseEntity<CommonResponse<List<TopWriterResponse>>> getTopWriters(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "30") int days) {
        return CommonResponse.ok(statsService.getTopWriters(limit, days), "가장 글을 많이 쓴 작성자 목록 조회를 성공했습니다.");
    }

    @GetMapping("/users/top-commenters")
    public ResponseEntity<CommonResponse<List<TopCommenterResponse>>> getTopCommenters(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "30") int days) {
        return CommonResponse.ok(statsService.getTopCommenters(limit, days),
                "가장 댓글을 많이 쓴 작성자 목록 조회를 성공했습니다.");
    }
}
