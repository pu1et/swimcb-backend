package com.project.swimcb.bo.freeswimming.adapter.out;

import static com.project.swimcb.db.entity.QFreeSwimmingEntity.freeSwimmingEntity;
import static com.project.swimcb.db.entity.QSwimmingInstructorEntity.swimmingInstructorEntity;
import static com.project.swimcb.db.entity.QTicketEntity.ticketEntity;
import static com.project.swimcb.db.entity.TicketTargetType.FREE_SWIMMING;
import static com.querydsl.core.types.Projections.constructor;

import com.project.swimcb.bo.freeswimming.application.port.out.FindBoFreeSwimmingDsGateway;
import com.project.swimcb.bo.freeswimming.domain.BoFreeSwimming;
import com.project.swimcb.bo.freeswimming.domain.BoFreeSwimming.Lifeguard;
import com.project.swimcb.bo.freeswimming.domain.BoFreeSwimming.Ticket;
import com.project.swimcb.bo.freeswimming.domain.BoFreeSwimming.TicketPriceRange;
import com.project.swimcb.bo.freeswimming.domain.BoFreeSwimming.Time;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.Builder;
import lombok.NonNull;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
class FindBoFreeSwimmingDataMapper implements FindBoFreeSwimmingDsGateway {

  private final JPAQueryFactory queryFactory;

  public FindBoFreeSwimmingDataMapper(EntityManager entityManager) {
    this.queryFactory = new JPAQueryFactory(entityManager);
  }

  @Override
  public List<BoFreeSwimming> findBoFreeSwimming(
      @NonNull Long swimmingPoolId,
      @NonNull LocalDate yearMonth
  ) {
    val result = queryFactory.select(constructor(QueryBoFreeSwimming.class,
            freeSwimmingEntity.id,
            freeSwimmingEntity.daysOfWeek,
            freeSwimmingEntity.startTime,
            freeSwimmingEntity.endTime,
            freeSwimmingEntity.capacity,
            freeSwimmingEntity.isVisible,
            freeSwimmingEntity.lifeguard.id,
            freeSwimmingEntity.lifeguard.name,
            ticketEntity.id,
            ticketEntity.name,
            ticketEntity.price
        ))
        .from(freeSwimmingEntity)
        .leftJoin(freeSwimmingEntity.lifeguard, swimmingInstructorEntity)
        .join(ticketEntity).on(ticketEntity.targetId.eq(freeSwimmingEntity.id))
        .where(
            freeSwimmingEntity.swimmingPool.id.eq(swimmingPoolId),
            freeSwimmingEntity.yearMonth.eq(yearMonth),
            ticketEntity.targetType.eq(FREE_SWIMMING)
        )
        .fetch();

    return result
        .stream()
        .collect(Collectors.groupingBy(i -> i.freeSwimmingId))
        .entrySet()
        .stream()
        .map(i -> {
              val key = i.getKey();
              val value = i.getValue().getFirst();
              val days = days(value.daysOfWeek());
              val ticketMap = i.getValue().stream()
                  .collect(Collectors.toMap(
                      QueryBoFreeSwimming::ticketId,
                      j -> j
                  ));
              val lifeguard = lifeguard(value.lifeguardId(), value.lifeguardName());
              val minimumTicketPrice = ticketMap.values().stream()
                  .mapToInt(QueryBoFreeSwimming::ticketPrice).min().orElse(0);
              val maximumTicketPrice = ticketMap.values().stream()
                  .mapToInt(QueryBoFreeSwimming::ticketPrice).max().orElse(0);

              return BoFreeSwimming.builder()
                  .freeSwimmingId(key)
                  .days(days)
                  .time(
                      Time.builder()
                          .startTime(value.startTime())
                          .endTime(value.endTime())
                          .build()
                  )
                  .lifeguard(lifeguard)
                  .ticketPriceRange(
                      TicketPriceRange.builder()
                          .minimumPrice(minimumTicketPrice)
                          .maximumPrice(maximumTicketPrice)
                          .build()
                  )
                  .tickets(
                      ticketMap.entrySet()
                          .stream()
                          .map(j -> Ticket.builder()
                              .id(j.getKey())
                              .name(j.getValue().ticketName())
                              .price(j.getValue().ticketPrice())
                              .build()
                          )
                          .toList()
                  )
                  .capacity(value.capacity())
                  .isExposed(value.isExposed())
                  .build();
            }
        )
        .toList();

  }

  private List<DayOfWeek> days(int days) {
    return IntStream.range(0, 7)
        .filter(i -> (days & (1 << (6 - i))) != 0)
        .mapToObj(i -> DayOfWeek.of(i + 1))
        .toList();
  }

  private Lifeguard lifeguard(Long lifeguardId, String lifeguardName) {
    if (lifeguardId == null) {
      return null;
    }
    return Lifeguard.builder()
        .id(lifeguardId)
        .name(lifeguardName)
        .build();
  }

  @Builder
  public record QueryBoFreeSwimming(
      @NonNull Long freeSwimmingId,
      @NonNull Integer daysOfWeek,
      @NonNull LocalTime startTime,
      @NonNull LocalTime endTime,
      @NonNull Integer capacity,
      @NonNull Boolean isExposed,
      Long lifeguardId,
      String lifeguardName,
      @NonNull Long ticketId,
      @NonNull String ticketName,
      @NonNull Integer ticketPrice
  ) {

  }

}
