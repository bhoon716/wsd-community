package wsd.community.domain.stats.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter
public final class TopCommenterResponse {

    @Setter
    private int rank;
    private final Long userId;
    private final String username;
    private final String email;
    private final Long commentCount;

    @QueryProjection
    public TopCommenterResponse(Long userId, String username, String email, Long commentCount) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.commentCount = commentCount;
    }
}
