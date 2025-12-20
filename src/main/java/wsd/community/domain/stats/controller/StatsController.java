package wsd.community.domain.stats.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wsd.community.domain.stats.response.TopWriterResponse;
import wsd.community.domain.stats.service.StatsService;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    @GetMapping("/users/top-writers")
    public ResponseEntity<List<TopWriterResponse>> getTopWriters(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "30") int days) {

        List<TopWriterResponse> response = statsService.getTopWriters(limit, days);
        return ResponseEntity.ok(response);
    }
}
