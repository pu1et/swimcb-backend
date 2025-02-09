package com.project.swimcb.notice.adapter.out;

import static com.project.swimcb.notice.domain.QNotice.notice;

import com.project.swimcb.notice.application.out.SearchNoticesDsGateway;
import com.project.swimcb.notice.domain.Notice;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SearchNoticesDataMapper implements SearchNoticesDsGateway {

  private final JPAQueryFactory queryFactory;

  @Override
  public Page<Notice> searchNotices(@NonNull String keyword, @NonNull Pageable pageable) {

    val notices = queryFactory.selectFrom(notice)
        .where(notice.title.like(keyword))
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    val totalCount = queryFactory.select(notice.count())
        .from(notice)
        .where(notice.title.like(keyword))
        .fetchOne();

    return new PageImpl<>(notices, pageable, totalCount != null ? totalCount : 0);
  }
}
