package com.project.swimcb.bo.clone.adapter.out;

import static com.project.swimcb.db.entity.QFreeSwimmingEntity.freeSwimmingEntity;
import static com.project.swimcb.db.entity.QSwimmingInstructorEntity.swimmingInstructorEntity;
import static com.project.swimcb.db.entity.QSwimmingPoolEntity.swimmingPoolEntity;
import static com.project.swimcb.db.entity.QTicketEntity.ticketEntity;
import static com.project.swimcb.db.entity.TicketTargetType.FREE_SWIMMING;
import static com.querydsl.core.types.Projections.constructor;

import com.project.swimcb.bo.clone.application.port.out.CopyFreeSwimmingDsGateway;
import com.project.swimcb.bo.freeswimming.domain.DaysOfWeek;
import com.querydsl.core.annotations.QueryProjection;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
class CopyFreeSwimmingDataMapper implements CopyFreeSwimmingDsGateway {

  private final JPAQueryFactory queryFactory;

  @Override
  public List<FreeSwimmingCopyCandidate> findAllFreeSwimmingsByMonth(@NonNull YearMonth month) {

    return queryFactory.select(constructor(QueryFreeSwimmingCopyCandidate.class,
            swimmingPoolEntity.id,
            freeSwimmingEntity.id,
            freeSwimmingEntity.daysOfWeek,
            freeSwimmingEntity.startTime,
            freeSwimmingEntity.endTime,
            swimmingInstructorEntity.id,
            ticketEntity.name,
            ticketEntity.price,
            freeSwimmingEntity.capacity
        ))
        .from(swimmingPoolEntity)
        .join(freeSwimmingEntity).on(freeSwimmingEntity.swimmingPool.eq(swimmingPoolEntity))
        .leftJoin(freeSwimmingEntity.lifeguard, swimmingInstructorEntity)
        .join(ticketEntity).on(
            ticketEntity.targetType.eq(FREE_SWIMMING),
            ticketEntity.targetId.eq(freeSwimmingEntity.id)
        )
        .where(
            swimmingPoolEntity.name.isNotNull(),
            swimmingPoolEntity.address.isNotNull(),
            swimmingPoolEntity.latitude.isNotNull(),
            swimmingPoolEntity.longitude.isNotNull(),

            freeSwimmingEntity.yearMonth.eq(month.atDay(1)),
            freeSwimmingEntity.isVisible.isTrue(),
            freeSwimmingEntity.isCanceled.isFalse(),

            ticketEntity.isDeleted.isFalse()
        )
        .fetch()
        .stream()
        .collect(Collectors.groupingBy(i -> i.freeSwimmingId))
        .values()
        .stream()
        .map(candidates -> {
          val first = candidates.get(0);
          return FreeSwimmingCopyCandidate.builder()
              .swimmingPoolId(first.swimmingPoolId)
              .days(DaysOfWeek.of(first.days))
              .time(
                  FreeSwimmingCopyCandidate.Time.builder()
                      .startTime(first.startTime)
                      .endTime(first.endTime)
                      .build()
              )
              .lifeguardId(first.lifeguardId)
              .tickets(candidates.stream()
                  .map(ticket -> FreeSwimmingCopyCandidate.Ticket.builder()
                      .name(ticket.ticketName)
                      .price(ticket.ticketPrice)
                      .build())
                  .toList())
              .capacity(first.capacity)
              .build();
        })
        .toList();
  }

  protected record QueryFreeSwimmingCopyCandidate(
      long swimmingPoolId,
      long freeSwimmingId,
      int days,
      LocalTime startTime,
      LocalTime endTime,
      Long lifeguardId,
      String ticketName,
      int ticketPrice,
      Integer capacity
  ) {

    @QueryProjection
    public QueryFreeSwimmingCopyCandidate {
    }

  }

}
