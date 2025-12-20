package wsd.community.domain.comment.repository;

import static wsd.community.domain.comment.entity.QComment.comment;
import static wsd.community.domain.user.entity.QUser.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import wsd.community.domain.comment.response.CommentResponse;
import wsd.community.domain.comment.response.QCommentResponse;
import wsd.community.domain.stats.response.TopCommenterResponse;
import wsd.community.domain.stats.response.QTopCommenterResponse;
import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<CommentResponse> findAllByPostId(Long postId) {
        return queryFactory
                .select(new QCommentResponse(
                        comment.id,
                        comment.content,
                        comment.user.name,
                        comment.createdAt,
                        comment.updatedAt,
                        comment.likeCount,
                        comment.isHidden))
                .from(comment)
                .leftJoin(comment.user, user)
                .where(comment.post.id.eq(postId))
                .orderBy(comment.createdAt.asc())
                .fetch();
    }

    @Override
    public List<TopCommenterResponse> findTopCommenters(
            LocalDateTime startDate,
            LocalDateTime endDate, int limit) {
        return queryFactory
                .select(new QTopCommenterResponse(
                        comment.user.id,
                        comment.user.name,
                        comment.user.email,
                        comment.count().as("commentCount")))
                .from(comment)
                .join(comment.user, user)
                .where(comment.createdAt.between(startDate, endDate))
                .groupBy(comment.user.id, comment.user.name, comment.user.email)
                .orderBy(comment.count().desc())
                .limit(limit)
                .fetch();
    }
}
