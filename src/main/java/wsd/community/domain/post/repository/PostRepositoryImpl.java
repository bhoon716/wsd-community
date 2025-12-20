package wsd.community.domain.post.repository;

import static wsd.community.domain.post.entity.QPost.post;
import static wsd.community.domain.user.entity.QUser.user;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import wsd.community.domain.post.entity.PostType;
import wsd.community.domain.post.response.PostSummaryResponse;
import wsd.community.domain.post.response.QPostSummaryResponse;
import wsd.community.domain.stats.response.QTopWriterResponse;
import wsd.community.domain.stats.response.TopWriterResponse;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<PostSummaryResponse> search(PostType type, String keyword, Pageable pageable) {
        BooleanExpression[] where = {
                typeEq(type),
                titleContains(keyword)
        };

        return getPostSummaryPage(where, pageable);
    }

    @Override
    public Page<PostSummaryResponse> findMyPosts(Long userId, Pageable pageable) {
        BooleanExpression where = writerIdEq(userId);

        return getPostSummaryPage(new BooleanExpression[] { where }, pageable);
    }

    private Page<PostSummaryResponse> getPostSummaryPage(BooleanExpression[] where, Pageable pageable) {
        List<PostSummaryResponse> content = queryFactory
                .select(new QPostSummaryResponse(
                        post.id,
                        post.title,
                        post.type,
                        post.createdAt,
                        post.updatedAt,
                        post.user.name,
                        post.likeCount,
                        post.isHidden,
                        post.isPinned))
                .from(post)
                .leftJoin(post.user, user)
                .where(where)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifiers(pageable))
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(post.count())
                .from(post)
                .where(where);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public List<TopWriterResponse> findTopWriters(LocalDateTime startDate, LocalDateTime endDate, int limit) {
        return queryFactory
                .select(new QTopWriterResponse(
                        post.user.id,
                        post.user.name,
                        post.user.email,
                        post.count().as("postCount")))
                .from(post)
                .join(post.user, user)
                .where(dateBetween(startDate, endDate))
                .groupBy(post.user.id, post.user.name, post.user.email)
                .orderBy(post.count().desc())
                .limit(limit)
                .fetch();
    }

    private OrderSpecifier<?>[] getOrderSpecifiers(Pageable pageable) {
        List<OrderSpecifier<?>> orders = new ArrayList<>();

        orders.add(new OrderSpecifier<>(Order.DESC, post.isPinned));

        if (!pageable.getSort().isEmpty()) {
            for (Sort.Order order : pageable.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                switch (order.getProperty()) {
                    case "likes":
                        orders.add(new OrderSpecifier<>(direction, post.likeCount));
                        break;
                    case "title":
                        orders.add(new OrderSpecifier<>(direction, post.title));
                        break;
                    case "createdAt":
                        orders.add(new OrderSpecifier<>(direction, post.createdAt));
                        break;
                }
            }
        }

        orders.add(new OrderSpecifier<>(Order.DESC, post.id));

        return orders.toArray(OrderSpecifier[]::new);
    }

    private BooleanExpression typeEq(PostType type) {
        return type != null ? post.type.eq(type) : null;
    }

    private BooleanExpression titleContains(String keyword) {
        return StringUtils.hasText(keyword) ? post.title.contains(keyword) : null;
    }

    private BooleanExpression writerIdEq(Long id) {
        return id != null ? post.user.id.eq(id) : null;
    }

    private BooleanExpression dateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return post.createdAt.between(startDate, endDate);
    }
}
