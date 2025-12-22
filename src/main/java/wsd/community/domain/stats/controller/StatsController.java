package wsd.community.domain.stats.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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
@Tag(name = "Stats", description = "통계 API")
public class StatsController {

    private final StatsService statsService;

    @GetMapping("/users/top-writers")
    @Operation(summary = "가장 글을 많이 쓴 작성자 조회", description = "지정된 기간 동안 가장 글을 많이 쓴 작성자 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "가장 글을 많이 쓴 작성자 조회 성공 예시", value = """
            {
                "code": "SUCCESS",
                "message": "가장 글을 많이 쓴 작성자 목록 조회를 성공했습니다.",
                "data": [
                    {
                        "rank": 1,
                        "userId": 10,
                        "username": "홍길동",
                        "email": "hong@example.com",
                        "postCount": 30
                    },
                    {
                        "rank": 2,
                        "userId": 20,
                        "username": "김철수",
                        "email": "kim@example.com",
                        "postCount": 25
                    }
                ]
            }
            """)))
    public ResponseEntity<CommonResponse<List<TopWriterResponse>>> getTopWriters(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "30") int days) {
        return CommonResponse.ok(statsService.getTopWriters(limit, days), "가장 글을 많이 쓴 작성자 목록 조회를 성공했습니다.");
    }

    @GetMapping("/users/top-commenters")
    @Operation(summary = "가장 댓글을 많이 쓴 작성자 조회", description = "지정된 기간 동안 가장 댓글을 많이 쓴 작성자 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "가장 댓글을 많이 쓴 작성자 조회 성공 예시", value = """
            {
                "code": "SUCCESS",
                "message": "가장 댓글을 많이 쓴 작성자 목록 조회를 성공했습니다.",
                "data": [
                    {
                        "rank": 1,
                        "userId": 15,
                        "username": "이영희",
                        "email": "lee@example.com",
                        "commentCount": 50
                    },
                    {
                        "rank": 2,
                        "userId": 25,
                        "username": "박민수",
                        "email": "park@example.com",
                        "commentCount": 45
                    }
                ]
            }
            """)))
    public ResponseEntity<CommonResponse<List<TopCommenterResponse>>> getTopCommenters(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "30") int days) {
        return CommonResponse.ok(statsService.getTopCommenters(limit, days),
                "가장 댓글을 많이 쓴 작성자 목록 조회를 성공했습니다.");
    }
}
