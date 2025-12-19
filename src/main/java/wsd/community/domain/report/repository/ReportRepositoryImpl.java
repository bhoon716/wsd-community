package wsd.community.domain.report.repository;

import static wsd.community.domain.comment.entity.QComment.comment;
import static wsd.community.domain.post.entity.QPost.post;
import static wsd.community.domain.report.entity.QReport.report;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import wsd.community.domain.report.entity.ReportStatus;
import wsd.community.domain.report.entity.ReportType;
import wsd.community.domain.report.response.QReportDetailResponse;
import wsd.community.domain.report.response.QReportSummaryResponse;
import wsd.community.domain.report.response.ReportDetailResponse;
import wsd.community.domain.report.response.ReportSummaryResponse;
import wsd.community.domain.user.entity.QUser;
import wsd.community.domain.user.entity.User;

@Repository
@RequiredArgsConstructor
public class ReportRepositoryImpl implements ReportRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ReportSummaryResponse> getReports(ReportStatus status, ReportType type, Pageable pageable) {
        BooleanBuilder where = new BooleanBuilder();
        where.and(statusEq(status))
                .and(typeEq(type));

        return fetchSummary(pageable, where);
    }

    @Override
    public Page<ReportSummaryResponse> getMyReports(User reporter, Pageable pageable) {
        BooleanBuilder where = new BooleanBuilder();
        where.and(reporterEq(reporter));

        return fetchSummary(pageable, where);
    }

    private Page<ReportSummaryResponse> fetchSummary(Pageable pageable, BooleanBuilder conditions) {
        QUser reporterUser = new QUser("reporterUser");

        List<ReportSummaryResponse> content = queryFactory
                .select(new QReportSummaryResponse(
                        report.id,
                        report.reporter.id,
                        report.reporter.email,
                        report.post.id.coalesce(report.comment.id),
                        report.type,
                        report.reason,
                        report.targetTitle,
                        report.status,
                        report.createdAt))
                .from(report)
                .leftJoin(report.reporter, reporterUser)
                .leftJoin(report.post, post)
                .leftJoin(report.comment, comment)
                .where(conditions)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifiers(pageable))
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(report.count())
                .from(report)
                .leftJoin(report.reporter, reporterUser)
                .leftJoin(report.post, post)
                .leftJoin(report.comment, comment)
                .where(conditions);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public Optional<ReportDetailResponse> getReportDetail(Long reportId) {
        QUser reporterUser = new QUser("reporterUser");
        QUser resolverUser = new QUser("resolverUser");

        ReportDetailResponse content = queryFactory
                .select(new QReportDetailResponse(
                        report.id,
                        reporterUser.id,
                        reporterUser.email,
                        report.post.id.coalesce(report.comment.id),
                        report.type,
                        report.reason,
                        report.description,
                        report.targetTitle,
                        report.targetContent,
                        report.resolvedReason,
                        resolverUser.email,
                        report.resolvedAt,
                        report.action,
                        report.status,
                        report.createdAt))
                .from(report)
                .leftJoin(report.reporter, reporterUser)
                .leftJoin(report.resolvedBy, resolverUser)
                .leftJoin(report.post, post)
                .leftJoin(report.comment, comment)
                .where(report.id.eq(reportId))
                .fetchOne();

        return Optional.ofNullable(content);
    }

    private BooleanExpression statusEq(ReportStatus status) {
        return status != null ? report.status.eq(status) : null;
    }

    private BooleanExpression typeEq(ReportType type) {
        return type != null ? report.type.eq(type) : null;
    }

    private BooleanExpression reporterEq(User reporter) {
        return report.reporter.eq(reporter);
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
            orders.add(new OrderSpecifier<>(Order.DESC, report.createdAt));
        }
        orders.add(new OrderSpecifier<>(Order.DESC, report.id));

        return orders.toArray(new OrderSpecifier[0]);
    }
}
