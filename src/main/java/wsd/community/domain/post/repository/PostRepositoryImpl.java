package wsd.community.domain.post.repository;

import static wsd.community.domain.post.entity.QPost.post;
import static wsd.community.domain.user.entity.QUser.user;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
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
import wsd.community.domain.post.request.PostSearchCondition;
import wsd.community.domain.post.response.PostSummaryResponse;
import wsd.community.domain.post.response.QPostSummaryResponse;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<PostSummaryResponse> search(PostSearchCondition condition, Pageable pageable) {
        BooleanExpression[] where = {
                typeEq(condition.type()),
                titleOrContentContains(condition.keyword())
        };

        List<PostSummaryResponse> content = queryFactory
                .select(new QPostSummaryResponse(
                        post.id,
                        post.title,
                        post.type,
                        post.createdAt,
                        post.updatedAt,
                        post.user.name,
                        post.likeCount))
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

    private OrderSpecifier<?>[] getOrderSpecifiers(Pageable pageable) {
        List<OrderSpecifier<?>> orders = new ArrayList<>();

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

    private BooleanExpression titleOrContentContains(String keyword) {
        return StringUtils.hasText(keyword) ? post.title.contains(keyword).or(post.content.contains(keyword)) : null;
    }
}
