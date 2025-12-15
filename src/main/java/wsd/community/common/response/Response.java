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
public class Response<T> {

    private static final String SUCCESS_CODE = "SUCCESS";

    private final String code;
    private final String message;
    private final T data;

    public static <T> Response<T> success(T data, String message) {
        return new Response<>(
                SUCCESS_CODE,
                message,
                data);
    }

    private static <T> Response<T> noContentBody(String message) {
        return new Response<>(
                SUCCESS_CODE,
                message,
                null);
    }

    public static <T> ResponseEntity<Response<T>> ok(T data, String message) {
        return ResponseEntity.ok(Response.success(data, message));
    }

    public static <T> ResponseEntity<Response<T>> created(T data, URI location, String message) {
        return ResponseEntity.created(location).body(Response.success(data, message));
    }

    public static <T> ResponseEntity<Response<T>> noContent(String message) {
        return ResponseEntity.ok(Response.noContentBody(message));
    }
}
