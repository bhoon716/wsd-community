package wsd.community.domain.comment.repository;

import static wsd.community.domain.comment.entity.QComment.comment;
import static wsd.community.domain.user.entity.QUser.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import wsd.community.domain.comment.response.CommentResponse;
import wsd.community.domain.comment.response.QCommentResponse;

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
}
