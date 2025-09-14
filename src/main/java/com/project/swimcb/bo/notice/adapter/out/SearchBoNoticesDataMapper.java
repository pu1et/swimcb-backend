package com.project.swimcb.bo.notice.adapter.out;

import static com.project.swimcb.db.entity.QNoticeEntity.noticeEntity;

import com.project.swimcb.bo.notice.application.out.SearchNoticesDsGateway;
import com.project.swimcb.db.entity.NoticeEntity;
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
public class SearchBoNoticesDataMapper implements SearchNoticesDsGateway {

  private final JPAQueryFactory queryFactory;

  @Override
  public Page<NoticeEntity> searchNotices(@NonNull String keyword, @NonNull Pageable pageable) {

    val notices = queryFactory.selectFrom(noticeEntity)
        .where(noticeEntity.title.like(keyword))
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    val totalCount = queryFactory.select(noticeEntity.count())
        .from(noticeEntity)
        .where(noticeEntity.title.like(keyword))
        .fetchOne();

    return new PageImpl<>(notices, pageable, totalCount != null ? totalCount : 0);
  }

}
