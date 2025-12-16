package wsd.community.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wsd.community.common.response.Response;
import wsd.community.security.auth.CustomUserDetails;

import wsd.community.domain.user.response.UserResponse;
import wsd.community.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Tag(name = "Users", description = "사용자 관리 API")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    @Operation(summary = "내 정보 조회", description = "현재 로그인한 사용자의 프로필을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "내 정보 조회 성공 예시", value = """
            {
                "isSuccess": true,
                "message": "내 정보 조회 성공",
                "payload": {
                    "userId": 1,
                    "name": "홍길동",
                    "email": "hong@test.com"
                }
            }
            """)))
    public ResponseEntity<Response<UserResponse>> getMyProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        UserResponse response = userService.getUser(userDetails.getUserId());
        return Response.ok(response, "내 정보 조회 성공");
    }

    @DeleteMapping("/me")
    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴를 처리합니다.")
    @ApiResponse(responseCode = "204", description = "탈퇴 성공", content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "회원 탈퇴 성공 예시", value = """
            {
                "isSuccess": true,
                "message": "회원 탈퇴 성공",
                "payload": null
            }
            """)))
    public ResponseEntity<Response<Void>> withdraw(@AuthenticationPrincipal CustomUserDetails userDetails) {
        userService.withdraw(userDetails.getUserId());
        return Response.noContent("회원 탈퇴 성공");
    }
}
