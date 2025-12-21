package wsd.community.domain.comment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wsd.community.common.response.CommonResponse;
import wsd.community.domain.comment.request.CommentCreateRequest;
import wsd.community.domain.comment.request.CommentUpdateRequest;
import wsd.community.domain.comment.response.CommentResponse;
import wsd.community.domain.comment.service.CommentService;
import wsd.community.security.auth.CustomUserDetails;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "Comment", description = "댓글 API")
public class CommentController {

        private final CommentService commentService;

        @Operation(summary = "댓글 목록 조회", description = "특정 게시글의 댓글 목록을 조회합니다.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "댓글 목록 조회 성공", content = @Content(schema = @Schema(implementation = CommonResponse.class), examples = @ExampleObject(value = """
                                        {
                                          "status": "success",
                                          "message": "댓글 목록 조회 성공",
                                          "data": [
                                            {
                                              "id": 1,
                                              "content": "댓글 내용",
                                              "writerName": "작성자",
                                              "createdAt": "2024-01-01T12:00:00",
                                              "updatedAt": "2024-01-01T12:00:00",
                                              "likeCount": 0
                                            }
                                          ]
                                        }
                                        """)))
        })
        @GetMapping("/posts/{postId}/comments")
        public ResponseEntity<CommonResponse<List<CommentResponse>>> getComments(
                        @PathVariable Long postId) {
                List<CommentResponse> comments = commentService.getComments(postId);
                return CommonResponse.ok(comments, "댓글 목록 조회 성공");
        }

        @Operation(summary = "댓글 생성", description = "게시글에 댓글을 작성합니다.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "댓글 생성 성공", content = @Content(schema = @Schema(implementation = CommonResponse.class), examples = @ExampleObject(value = """
                                        {
                                          "status": "success",
                                          "message": "댓글 생성 성공",
                                          "data": 1
                                        }
                                        """))),
                        @ApiResponse(responseCode = "404", description = "게시글 없음", content = @Content(schema = @Schema(implementation = CommonResponse.class), examples = @ExampleObject(value = """
                                        {
                                          "status": "error",
                                          "message": "게시글을 찾을 수 없습니다."
                                        }
                                        """)))
        })
        @PostMapping("/posts/{postId}/comments")
        public ResponseEntity<CommonResponse<Long>> createComment(
                        @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails,
                        @PathVariable Long postId,
                        @Valid @RequestBody CommentCreateRequest request) {
                Long commentId = commentService.createComment(userDetails.getUserId(), postId, request);
                return CommonResponse.created(commentId, URI.create("/api/v1/comments/" + commentId), "댓글 생성 성공");
        }

        @Operation(summary = "댓글 수정", description = "작성자가 댓글을 수정합니다.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "댓글 수정 성공", content = @Content(schema = @Schema(implementation = CommonResponse.class), examples = @ExampleObject(value = """
                                        {
                                          "status": "success",
                                          "message": "댓글 수정 성공",
                                          "data": 1
                                        }
                                        """))),
                        @ApiResponse(responseCode = "403", description = "수정 권한 없음", content = @Content(schema = @Schema(implementation = CommonResponse.class), examples = @ExampleObject(value = """
                                        {
                                          "status": "error",
                                          "message": "권한이 없습니다."
                                        }
                                        """)))
        })
        @PutMapping("/comments/{commentId}")
        public ResponseEntity<CommonResponse<Long>> updateComment(
                        @AuthenticationPrincipal CustomUserDetails userDetails,
                        @PathVariable Long commentId,
                        @Valid @RequestBody CommentUpdateRequest request) {
                Long updatedCommentId = commentService.updateComment(userDetails.getUserId(), commentId, request);
                return CommonResponse.ok(updatedCommentId, "댓글 수정 성공");
        }

        @Operation(summary = "댓글 삭제", description = "작성자가 댓글을 삭제합니다.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "댓글 삭제 성공", content = @Content(schema = @Schema(implementation = CommonResponse.class), examples = @ExampleObject(value = """
                                        {
                                          "status": "success",
                                          "message": "댓글 삭제 성공",
                                          "data": null
                                        }
                                        """)))
        })
        @DeleteMapping("/comments/{commentId}")
        public ResponseEntity<CommonResponse<Void>> deleteComment(
                        @AuthenticationPrincipal CustomUserDetails userDetails,
                        @PathVariable Long commentId) {
                commentService.deleteComment(userDetails.getUserId(), commentId);
                return CommonResponse.noContent("댓글 삭제 성공");
        }

        @Operation(summary = "댓글 좋아요 토글", description = "댓글에 좋아요를 누르거나 취소합니다. (true: 좋아요 설정됨, false: 좋아요 해제됨)")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "좋아요 토글 성공", content = @Content(schema = @Schema(implementation = CommonResponse.class), examples = @ExampleObject(value = """
                                        {
                                          "status": "success",
                                          "message": "좋아요 설정 성공"
                                        }
                                        """)))
        })
        @PostMapping("/comments/{commentId}/likes")
        public ResponseEntity<CommonResponse<Void>> toggleLike(
                        @AuthenticationPrincipal CustomUserDetails userDetails,
                        @PathVariable Long commentId) {
                boolean isLiked = commentService.toggleLike(userDetails.getUserId(), commentId);
                return CommonResponse.noContent(isLiked ? "좋아요 설정 성공" : "좋아요 취소 성공");
        }
}
