package wsd.community.common.error;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import wsd.community.common.response.ErrorResponse;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(
            HttpServletRequest request, CustomException e) {

        ErrorCode error = e.getErrorCode();
        Map<String, Object> details = detailOrNull(e.getDetail());

        log.error("[ERROR] code={}, message={}, path={}, detail={}",
                error.getCode(), error.getMessage(), request.getRequestURI(), e.getDetail());

        return buildResponse(error, request.getRequestURI(), details);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            HttpServletRequest request, MethodArgumentNotValidException e) {

        Map<String, Object> details = new HashMap<>();
        e.getBindingResult().getFieldErrors()
                .forEach(error -> details.put(error.getField(), error.getDefaultMessage()));

        log.warn("[WARN] 유효성 검증 실패: path={}, details={}", request.getRequestURI(), details);

        return buildResponse(ErrorCode.INVALID_INPUT, request.getRequestURI(), details);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponse> handleExpiredJwtException(
            HttpServletRequest request, ExpiredJwtException e) {

        log.warn("[WARN] 만료된 JWT: path={}, message={}", request.getRequestURI(), e.getMessage());

        Map<String, Object> details = detailOrNull("만료된 토큰입니다.");
        return buildResponse(ErrorCode.INVALID_TOKEN, request.getRequestURI(), details);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorResponse> handleJwtException(
            HttpServletRequest request, JwtException e) {

        log.warn("[WARN] 유효하지 않은 JWT: path={}, message={}", request.getRequestURI(), e.getMessage());

        Map<String, Object> details = detailOrNull("유효하지 않은 토큰입니다.");
        return buildResponse(ErrorCode.INVALID_TOKEN, request.getRequestURI(), details);
    }

    @ExceptionHandler({ AuthorizationDeniedException.class, AccessDeniedException.class })
    public ResponseEntity<ErrorResponse> handleAccessDenied(
            HttpServletRequest request, RuntimeException e) {

        log.warn("[WARN] 접근 거부됨: path={}, message={}", request.getRequestURI(), e.getMessage());

        return buildResponse(ErrorCode.FORBIDDEN, request.getRequestURI(), null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(
            HttpServletRequest request, Exception e) {

        log.error("[ERROR] 예기치 못한 오류: path={}, exception={}",
                request.getRequestURI(), e.getClass().getSimpleName());

        Map<String, Object> details = detailOrNull(e.getMessage());
        return buildResponse(ErrorCode.INTERNAL_ERROR, request.getRequestURI(), details);
    }

    private ResponseEntity<ErrorResponse> buildResponse(
            ErrorCode error, String path, Map<String, Object> details) {
        return ResponseEntity
                .status(error.getStatus())
                .body(ErrorResponse.of(error, path, details));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFoundException(
            HttpServletRequest request, NoResourceFoundException e) {
        log.warn("[WARN] 리소스를 찾을 수 없음: path={}", request.getRequestURI());
        return buildResponse(ErrorCode.NOT_FOUND, request.getRequestURI(), null);
    }

    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(
            HttpServletRequest request, org.springframework.dao.DataIntegrityViolationException e) {

        log.warn("[WARN] 데이터 무결성 위반: path={}, message={}", request.getRequestURI(), e.getMessage());

        return buildResponse(ErrorCode.INVALID_INPUT, request.getRequestURI(),
                detailOrNull("데이터처리 중 충돌이 발생했습니다. (중복 데이터 등)"));
    }

    private Map<String, Object> detailOrNull(String detailMessage) {
        return detailMessage == null ? null : Map.of("detail", detailMessage);
    }
}
