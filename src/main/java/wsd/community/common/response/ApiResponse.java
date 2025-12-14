package wsd.community.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.net.URI;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
@JsonInclude(Include.NON_NULL)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResponse<T> {

    private static final String SUCCESS_CODE = "SUCCESS";

    private final String code;
    private final String message;
    private final T data;

    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(
                SUCCESS_CODE,
                message,
                data);
    }

    private static <T> ApiResponse<T> noContentBody(String message) {
        return new ApiResponse<>(
                SUCCESS_CODE,
                message,
                null);
    }

    public static <T> ResponseEntity<ApiResponse<T>> ok(T data, String message) {
        return ResponseEntity.ok(ApiResponse.success(data, message));
    }

    public static <T> ResponseEntity<ApiResponse<T>> created(T data, URI location, String message) {
        return ResponseEntity.created(location).body(ApiResponse.success(data, message));
    }

    public static <T> ResponseEntity<ApiResponse<T>> noContent(String message) {
        return ResponseEntity.ok(ApiResponse.noContentBody(message));
    }
}
