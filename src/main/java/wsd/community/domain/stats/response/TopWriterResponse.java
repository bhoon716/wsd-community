package wsd.community.domain.stats.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter
public final class TopWriterResponse {

    @Setter
    private int rank;
    private final Long userId;
    private final String username;
    private final String email;
    private final Long postCount;

    @QueryProjection
    public TopWriterResponse(Long userId, String username, String email, Long postCount) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.postCount = postCount;
    }
}
