package com.project.swimcb.bo.faq.adapter.out;

import static com.project.swimcb.bo.faq.domain.QFaq.faq;

import com.project.swimcb.bo.faq.application.out.SearchFaqsDsGateway;
import com.project.swimcb.bo.faq.domain.Faq;
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
public class SearchFaqsDataMapper implements SearchFaqsDsGateway {

  private final JPAQueryFactory queryFactory;

  @Override
  public Page<Faq> searchFaqs(@NonNull String keyword, @NonNull Pageable pageable) {

    val faqs = queryFactory.selectFrom(faq)
        .where(faq.title.like(keyword))
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    val totalCount = queryFactory.select(faq.count())
        .from(faq)
        .where(faq.title.like(keyword))
        .fetchOne();

    return new PageImpl<>(faqs, pageable, totalCount != null ? totalCount : 0);
  }
}
