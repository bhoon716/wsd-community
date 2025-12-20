package wsd.community.domain.stats.response;

import io.swagger.v3.oas.annotations.media.Schema;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Schema(description = "가장 글을 많이 쓴 작성자 응답 DTO")
public final class TopWriterResponse {

    @Setter
    @Schema(description = "순위", example = "1")
    private int rank;
    @Schema(description = "사용자 ID", example = "10")
    private final Long userId;
    @Schema(description = "사용자 이름", example = "홍길동")
    private final String username;
    @Schema(description = "이메일", example = "hong@example.com")
    private final String email;
    @Schema(description = "게시글 수", example = "30")
    private final Long postCount;

    @QueryProjection
    public TopWriterResponse(Long userId, String username, String email, Long postCount) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.postCount = postCount;
    }
}
