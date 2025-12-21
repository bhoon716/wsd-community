package wsd.community.domain.post.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wsd.community.common.response.CommonResponse;
import wsd.community.domain.post.entity.PostType;
import wsd.community.domain.post.request.PostCreateRequest;
import wsd.community.domain.post.request.PostUpdateRequest;
import wsd.community.domain.post.response.PostDetailResponse;
import wsd.community.domain.post.response.PostSummaryResponse;
import wsd.community.domain.post.service.PostService;
import wsd.community.security.auth.CustomUserDetails;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Tag(name = "Post", description = "게시글 API")
public class PostController {

    private final PostService postService;

    @GetMapping
    @Operation(summary = "게시글 목록 조회 (검색)", description = "조건에 맞는 게시글 목록을 페이징하여 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "게시글 목록 조회 성공 예시", value = """
            {
                "code": "SUCCESS",
                "message": "게시글 목록 조회 성공",
                "data": {
                    "content": [
                        {
                            "id": 1,
                            "title": "게시글 제목 1",
                            "type": "GENERAL",
                            "createdAt": "2024-01-01T12:00:00",
                            "updatedAt": "2024-01-01T12:00:00",
                            "writerName": "홍길동"
                        },
                        {
                            "id": 2,
                            "title": "게시글 제목 2",
                            "type": "QNA",
                            "createdAt": "2024-01-02T15:00:00",
                            "updatedAt": "2024-01-02T15:00:00",
                            "writerName": "김철수"
                        }
                    ],
                    "pageable": {
                        "sort": {
                            "empty": false,
                            "sorted": true,
                            "unsorted": false
                        },
                        "offset": 0,
                        "pageNumber": 0,
                        "pageSize": 20,
                        "paged": true,
                        "unpaged": false
                    },
                    "last": true,
                    "totalElements": 2,
                    "totalPages": 1,
                    "size": 20,
                    "number": 0,
                    "sort": {
                        "empty": false,
                        "sorted": true,
                        "unsorted": false
                    },
                    "first": true,
                    "numberOfElements": 2,
                    "empty": false
                }
            }
            """)))
    public ResponseEntity<CommonResponse<Page<PostSummaryResponse>>> searchPosts(
            @RequestParam(required = false) PostType type,
            @RequestParam(required = false) String keyword,
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PostSummaryResponse> response = postService.searchPosts(type, keyword, pageable);
        return CommonResponse.ok(response, "게시글 목록 조회 성공");
    }

    @GetMapping("/my")
    @Operation(summary = "내 게시글 목록 조회", description = "현재 로그인한 사용자가 작성한 게시글 목록을 페이징하여 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "내 게시글 목록 조회 성공 예시", value = """
            {
                "code": "SUCCESS",
                "message": "내 게시글 목록 조회 성공",
                "data": {
                    "content": [
                        {
                            "id": 1,
                            "title": "내 게시글 제목 1",
                            "type": "GENERAL",
                            "createdAt": "2024-01-01T12:00:00",
                            "updatedAt": "2024-01-01T12:00:00",
                            "writerName": "홍길동"
                        },
                        {
                            "id": 2,
                            "title": "내 게시글 제목 2",
                            "type": "QNA",
                            "createdAt": "2024-01-02T15:00:00",
                            "updatedAt": "2024-01-02T15:00:00",
                            "writerName": "홍길동"
                        }
                    ],
                    "pageable": {
                        "sort": {
                            "empty": false,
                            "sorted": true,
                            "unsorted": false
                        },
                        "offset": 0,
                        "pageNumber": 0,
                        "pageSize": 20,
                        "paged": true,
                        "unpaged": false
                    },
                    "last": true,
                    "totalElements": 2,
                    "totalPages": 1,
                    "size": 20,
                    "number": 0,
                    "sort": {
                        "empty": false,
                        "sorted": true,
                        "unsorted": false
                    },
                    "first": true,
                    "numberOfElements": 2,
                    "empty": false
                }
            }
            """)))
    public ResponseEntity<CommonResponse<Page<PostSummaryResponse>>> getMyPosts(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<PostSummaryResponse> response = postService.getMyPosts(userDetails.getUserId(), pageable);
        return CommonResponse.ok(response, "내 게시글 목록 조회 성공");
    }

    @GetMapping("/{id}")
    @Operation(summary = "게시글 상세 조회", description = "게시글의 상세 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "게시글 상세 조회 성공 예시", value = """
            {
                "code": "SUCCESS",
                "message": "게시글 상세 조회 성공",
                "data": {
                    "id": 1,
                    "title": "게시글 제목",
                    "content": "게시글 내용",
                    "type": "GENERAL",
                    "createdAt": "2024-01-01T12:00:00",
                    "updatedAt": "2024-01-01T12:00:00",
                    "writerName": "홍길동",
                    "likeCount": 10,
                    "comments": [
                        {
                            "id": 1,
                            "content": "댓글 내용",
                            "writerName": "댓글 작성자",
                            "createdAt": "2024-01-01T12:30:00",
                            "updatedAt": "2024-01-01T12:30:00",
                            "likeCount": 5
                        }
                    ]
                }
            }
            """)))
    public ResponseEntity<CommonResponse<PostDetailResponse>> getPost(@PathVariable Long id) {
        PostDetailResponse response = postService.getPost(id);
        return CommonResponse.ok(response, "게시글 상세 조회 성공");
    }

    @PostMapping
    @Operation(summary = "게시글 생성", description = "새로운 게시글을 생성합니다.")
    @ApiResponse(responseCode = "201", description = "생성 성공", content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "게시글 생성 성공 예시", value = """
            {
                "code": "SUCCESS",
                "message": "게시글이 성공적으로 생성되었습니다.",
                "data": 1
            }
            """)))
    public ResponseEntity<CommonResponse<Long>> createPost(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid PostCreateRequest request) {
        Long postId = postService.createPost(userDetails.getUserId(), request);
        return CommonResponse.created(postId, URI.create("/api/posts/" + postId), "게시글이 성공적으로 생성되었습니다.");
    }

    @PutMapping("/{postId}")
    @Operation(summary = "게시글 수정", description = "게시글을 수정합니다.")
    @ApiResponse(responseCode = "200", description = "수정 성공", content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "게시글 수정 성공 예시", value = """
            {
                "code": "SUCCESS",
                "message": "게시글 수정 성공",
                "data": 1
            }
            """)))
    public ResponseEntity<CommonResponse<Long>> updatePost(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long postId,
            @Valid @RequestBody PostUpdateRequest request) {
        return CommonResponse.ok(
                postService.updatePost(userDetails.getUserId(), postId, request),
                "게시글 수정 성공");
    }

    @PostMapping("/{postId}/pin")
    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER')")
    @Operation(summary = "게시글 고정 (관리자 전용)", description = "게시글을 고정하거나 고정 해제합니다. (true: 고정됨, false: 해제됨)")
    @ApiResponse(responseCode = "200", description = "고정 성공", content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "게시글 고정 성공 예시", value = """
            {
                "code": "SUCCESS",
                "message": "게시글 고정 성공"
            }
            """)))
    public ResponseEntity<CommonResponse<Void>> togglePin(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long postId) {
        boolean isPinned = postService.togglePin(userDetails.getUserId(), postId);
        return CommonResponse.noContent(isPinned ? "게시글 고정 성공" : "게시글 고정 해제 성공");
    }

    @DeleteMapping("/{postId}")
    @Operation(summary = "게시글 삭제", description = "게시글을 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "삭제 성공", content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "게시글 삭제 성공 예시", value = """
            {
                "code": "SUCCESS",
                "message": "게시글이 성공적으로 삭제되었습니다.",
                "data": null
            }
            """)))
    public ResponseEntity<CommonResponse<Void>> deletePost(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long postId) {
        postService.deletePost(userDetails.getUserId(), postId);
        return CommonResponse.noContent("게시글이 성공적으로 삭제되었습니다.");
    }

    @PostMapping("/{id}/likes")
    @Operation(summary = "게시글 좋아요 토글", description = "게시글에 좋아요를 누르거나 취소합니다. (true: 좋아요 설정됨, false: 좋아요 해제됨)")
    @ApiResponse(responseCode = "200", description = "토글 성공", content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "좋아요 토글 성공 예시", value = """
            {
                "code": "SUCCESS",
                "message": "좋아요 설정 성공"
            }
            """)))
    public ResponseEntity<CommonResponse<Void>> toggleLike(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id) {
        boolean isLiked = postService.toggleLike(userDetails.getUserId(), id);
        return CommonResponse.noContent(isLiked ? "좋아요 설정 성공" : "좋아요 취소 성공");
    }
}
