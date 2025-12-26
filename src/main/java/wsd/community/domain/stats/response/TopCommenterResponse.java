package wsd.community.domain.stats.response;

import io.swagger.v3.oas.annotations.media.Schema;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Schema(description = "가장 댓글을 많이 쓴 작성자 응답 DTO")
public class TopCommenterResponse {

    @Setter
    @Schema(description = "순위", example = "1")
    private int rank;

    @Schema(description = "사용자 ID", example = "10")
    private Long userId;

    @Schema(description = "사용자 이름", example = "홍길동")
    private String username;

    @Schema(description = "이메일", example = "hong@example.com")
    private String email;

    @Schema(description = "댓글 수", example = "50")
    private Long commentCount;

    @QueryProjection
    public TopCommenterResponse(Long userId, String username, String email, Long commentCount) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.commentCount = commentCount;
    }
}
