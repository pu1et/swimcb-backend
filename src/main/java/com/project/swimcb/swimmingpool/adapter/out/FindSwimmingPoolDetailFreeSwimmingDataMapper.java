package com.project.swimcb.swimmingpool.adapter.out;

import static com.project.swimcb.db.entity.QFreeSwimmingDayStatusEntity.freeSwimmingDayStatusEntity;
import static com.project.swimcb.db.entity.QFreeSwimmingEntity.freeSwimmingEntity;
import static com.project.swimcb.db.entity.QTicketEntity.ticketEntity;
import static com.project.swimcb.db.entity.TicketTargetType.FREE_SWIMMING;
import static com.project.swimcb.swimmingpool.domain.SwimmingPoolDetailFreeSwimming.FreeSwimming;
import static com.project.swimcb.swimmingpool.domain.SwimmingPoolDetailFreeSwimming.Ticket;
import static com.project.swimcb.swimmingpool.domain.SwimmingPoolDetailFreeSwimming.Time;
import static com.querydsl.core.types.Projections.constructor;

import com.project.swimcb.mypage.reservation.adapter.out.ClassDayOfWeek;
import com.project.swimcb.swimmingpool.application.out.FindSwimmingPoolDetailFreeSwimmingDsGateway;
import com.project.swimcb.swimmingpool.domain.FindSwimmingPoolDetailFreeSwimmingCondition;
import com.project.swimcb.swimmingpool.domain.SwimmingPoolDetailFreeSwimming;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class FindSwimmingPoolDetailFreeSwimmingDataMapper implements
    FindSwimmingPoolDetailFreeSwimmingDsGateway {

  private final JPAQueryFactory queryFactory;

  @Override
  public SwimmingPoolDetailFreeSwimming findSwimmingPoolDetailFreeSwimming(
      @NonNull FindSwimmingPoolDetailFreeSwimmingCondition condition) {

    val result = queryFactory.selectDistinct(constructor(QuerySwimmingPoolDetailFreeSwimming.class,
            freeSwimmingEntity.id,
            freeSwimmingEntity.startTime,
            freeSwimmingEntity.endTime,
            freeSwimmingEntity.daysOfWeek,
            ticketEntity.id,
            ticketEntity.name,
            ticketEntity.price
        ))
        .from(freeSwimmingEntity)
        .join(freeSwimmingDayStatusEntity)
        .on(freeSwimmingDayStatusEntity.freeSwimming.eq(freeSwimmingEntity))
        .join(ticketEntity).on(
            ticketEntity.targetId.eq(freeSwimmingEntity.id),
            ticketEntity.targetType.eq(FREE_SWIMMING)
        )
        .where(
            freeSwimmingEntity.swimmingPool.id.eq(condition.swimmingPoolId()),
            freeSwimmingEntity.yearMonth.eq(condition.month().atDay(1)),
            freeSwimmingEntity.isCanceled.isFalse(),

            freeSwimmingEntity.isCanceled.isFalse(),
            freeSwimmingEntity.isVisible.isTrue(),
            freeSwimmingDayStatusEntity.isClosed.isFalse(),
            freeSwimmingDayStatusEntity.isReservationBlocked.isFalse(),

            ticketEntity.isDeleted.isFalse()
        )
        .fetch();

    val contents = result
        .stream()
        .collect(Collectors.groupingBy(QuerySwimmingPoolDetailFreeSwimming::id))
        .values()
        .stream()
        .map(i -> {
              val firstValue = i.getFirst();
              val daysOfWeek = ClassDayOfWeek.of(firstValue.daysOfWeek());
              val minimumTicket = i
                  .stream()
                  .min(Comparator.comparing(QuerySwimmingPoolDetailFreeSwimming::ticketPrice))
                  .get();

              return FreeSwimming.builder()
                  .time(
                      Time.builder()
                          .startTime(firstValue.startTime())
                          .endTime(firstValue.endTime())
                          .build()
                  )
                  .daysOfWeek(daysOfWeek)
                  .ticket(
                      Ticket.builder()
                          .name(minimumTicket.ticketName())
                          .price(minimumTicket.ticketPrice())
                          .build()
                  )
                  .build();
            }
        )
        .toList();

    return new SwimmingPoolDetailFreeSwimming(contents);
  }

  @Builder
  public record QuerySwimmingPoolDetailFreeSwimming(
      long id,
      @NonNull LocalTime startTime,
      @NonNull LocalTime endTime,
      @NonNull Integer daysOfWeek,
      @NonNull Long ticketId,
      @NonNull String ticketName,
      @NonNull Integer ticketPrice
  ) {

  }

}
