package com.project.swimcb.swimmingpool.adapter.out;

import static com.project.swimcb.db.entity.QFreeSwimmingDayStatusEntity.freeSwimmingDayStatusEntity;
import static com.project.swimcb.db.entity.QFreeSwimmingEntity.freeSwimmingEntity;
import static com.project.swimcb.db.entity.QSwimmingPoolEntity.swimmingPoolEntity;

import com.project.swimcb.swimmingpool.application.out.FindSwimmingPoolDetailFreeAvailableDaysDsGateway;
import com.project.swimcb.swimmingpool.domain.FindSwimmingPoolDetailFreeSwimmingAvailableDaysCondition;
import com.project.swimcb.swimmingpool.domain.SwimmingPoolDetailFreeAvailableDays;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class FindSwimmingPoolDetailFreeAvailableDaysDataMapper implements
    FindSwimmingPoolDetailFreeAvailableDaysDsGateway {

  private final JPAQueryFactory queryFactory;

  @Override
  public SwimmingPoolDetailFreeAvailableDays findSwimmingPoolDetailFreeAvailableDays(
      @NonNull FindSwimmingPoolDetailFreeSwimmingAvailableDaysCondition condition) {

    val result = queryFactory.selectDistinct(
            freeSwimmingDayStatusEntity.dayOfMonth
        )
        .from(freeSwimmingDayStatusEntity)
        .join(freeSwimmingDayStatusEntity.freeSwimming, freeSwimmingEntity)
        .join(freeSwimmingEntity.swimmingPool, swimmingPoolEntity)
        .where(
            freeSwimmingEntity.yearMonth.eq(condition.month().atDay(1)),
            swimmingPoolEntity.id.eq(condition.swimmingPoolId())
        )
        .fetch();
    return new SwimmingPoolDetailFreeAvailableDays(result);
  }

}
