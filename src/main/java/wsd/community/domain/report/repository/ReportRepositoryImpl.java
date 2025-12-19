package wsd.community.domain.report.repository;

import static wsd.community.domain.comment.entity.QComment.comment;
import static wsd.community.domain.post.entity.QPost.post;
import static wsd.community.domain.report.entity.QReport.report;
import static wsd.community.domain.user.entity.QUser.user;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
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
import wsd.community.domain.report.response.QReportResponse;
import wsd.community.domain.report.response.ReportResponse;
import wsd.community.domain.report.entity.ReportStatus;
import wsd.community.domain.report.entity.ReportType;
import com.querydsl.core.types.dsl.BooleanExpression;

@Repository
@RequiredArgsConstructor
public class ReportRepositoryImpl implements ReportRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ReportResponse> getReports(ReportStatus status, ReportType type, Pageable pageable) {
        List<ReportResponse> content = queryFactory
                .select(new QReportResponse(
                        report.id,
                        report.reporter.id,
                        report.reporter.email,
                        report.post.id.coalesce(report.comment.id),
                        report.type,
                        report.reason,
                        report.description,
                        report.post.title,
                        report.post.content.coalesce(report.comment.content),
                        report.action,
                        report.status,
                        report.createdAt))
                .from(report)
                .leftJoin(report.reporter, user)
                .leftJoin(report.post, post)
                .leftJoin(report.comment, comment)
                .where(
                        eqStatus(status),
                        eqType(type))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifiers(pageable))
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(report.count())
                .from(report)
                .where(
                        eqStatus(status),
                        eqType(type));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression eqStatus(ReportStatus status) {
        return status != null ? report.status.eq(status) : null;
    }

    private BooleanExpression eqType(ReportType type) {
        return type != null ? report.type.eq(type) : null;
    }

    private OrderSpecifier<?>[] getOrderSpecifiers(Pageable pageable) {
        List<OrderSpecifier<?>> orders = new ArrayList<>();

        if (!pageable.getSort().isEmpty()) {
            for (Sort.Order order : pageable.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                switch (order.getProperty()) {
                    case "createdAt":
                        orders.add(new OrderSpecifier<>(direction, report.createdAt));
                        break;
                    case "status":
                        orders.add(new OrderSpecifier<>(direction, report.status));
                        break;
                    default:
                        break;
                }
            }
        }

        if (orders.isEmpty()) {
            orders.add(new OrderSpecifier<>(Order.DESC, report.id));
        }

        return orders.toArray(OrderSpecifier[]::new);
    }
}
