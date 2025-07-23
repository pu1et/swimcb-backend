package com.project.swimcb.swimmingpool.adapter.out;

import static com.project.swimcb.db.entity.QFavoriteEntity.favoriteEntity;
import static com.project.swimcb.db.entity.QFreeSwimmingDayStatusEntity.freeSwimmingDayStatusEntity;
import static com.project.swimcb.db.entity.QFreeSwimmingEntity.freeSwimmingEntity;
import static com.project.swimcb.db.entity.QSwimmingPoolEntity.swimmingPoolEntity;
import static com.project.swimcb.db.entity.QTicketEntity.ticketEntity;
import static com.project.swimcb.db.entity.TicketTargetType.FREE_SWIMMING;
import static com.querydsl.core.types.Projections.constructor;

import com.project.swimcb.favorite.domain.enums.FavoriteTargetType;
import com.project.swimcb.swimmingpool.application.out.FindSwimmingPoolDetailFreeSwimmingDetailDsGateway;
import com.project.swimcb.swimmingpool.domain.FindSwimmingPoolDetailFreeSwimmingDetailCondition;
import com.project.swimcb.swimmingpool.domain.SwimmingPoolDetailFreeSwimmingDetail;
import com.project.swimcb.swimmingpool.domain.SwimmingPoolDetailFreeSwimmingDetail.FreeSwimming;
import com.project.swimcb.swimmingpool.domain.SwimmingPoolDetailFreeSwimmingDetail.Ticket;
import com.project.swimcb.swimmingpool.domain.SwimmingPoolDetailFreeSwimmingDetail.Time;
import com.querydsl.core.annotations.QueryProjection;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalTime;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class FindSwimmingPoolDetailFreeSwimmingDetailDataMapper implements
    FindSwimmingPoolDetailFreeSwimmingDetailDsGateway {

  private final JPAQueryFactory queryFactory;

  @Override
  public SwimmingPoolDetailFreeSwimmingDetail findSwimmingPoolDetailFreeSwimmingDetail(
      @NonNull FindSwimmingPoolDetailFreeSwimmingDetailCondition condition) {

    val firstDayOfMonth = condition.date().withDayOfMonth(1);

    val contents = queryFactory
        .select(constructor(SwimmingPoolDetailFreeSwimmingDetailQuery.class,
            freeSwimmingDayStatusEntity.id,
            freeSwimmingEntity.startTime,
            freeSwimmingEntity.endTime,
            ticketEntity.id,
            ticketEntity.name,
            ticketEntity.price,
            favoriteEntity.id
        ))
        .from(freeSwimmingDayStatusEntity)
        .join(freeSwimmingDayStatusEntity.freeSwimming, freeSwimmingEntity)
        .join(freeSwimmingEntity.swimmingPool, swimmingPoolEntity)
        .join(ticketEntity).on(
            ticketEntity.targetId.eq(freeSwimmingEntity.id),
            ticketEntity.targetType.eq(FREE_SWIMMING)
        )
        .leftJoin(favoriteEntity).on(
            favoriteEntity.member.id.eq(condition.memberId()),
            favoriteEntity.targetType.eq(FavoriteTargetType.FREE_SWIMMING),
            favoriteEntity.targetId.eq(freeSwimmingDayStatusEntity.id)
        )
        .where(
            swimmingPoolEntity.id.eq(condition.swimmingPoolId()),
            freeSwimmingEntity.yearMonth.eq(firstDayOfMonth),
            freeSwimmingDayStatusEntity.dayOfMonth.eq(condition.date().getDayOfMonth())
        )
        .fetch()
        .stream()
        .collect(Collectors.groupingBy(SwimmingPoolDetailFreeSwimmingDetailQuery::dayStatusId))
        .entrySet()
        .stream()
        .map(i -> {
          val key = i.getKey();
          val firstValue = i.getValue().getFirst();
          val minimumTicketPrice = i.getValue().stream()
              .mapToInt(SwimmingPoolDetailFreeSwimmingDetailQuery::ticketPrice).min().orElse(0);

          return FreeSwimming.builder()
              .dayStatusId(key)
              .time(
                  Time.builder()
                      .startTime(firstValue.startTime())
                      .endTime(firstValue.endTime())
                      .build()
              )
              .minTicketPrice(minimumTicketPrice)
              .tickets(
                  i.getValue().stream()
                      .map(j -> Ticket.builder()
                          .id(j.ticketId())
                          .name(j.ticketName())
                          .price(j.ticketPrice())
                          .build()
                      ).toList()
              )
              .favoriteId(firstValue.favoriteId())
              .build();
        })
        .toList();
    return new SwimmingPoolDetailFreeSwimmingDetail(contents);
  }

  public record SwimmingPoolDetailFreeSwimmingDetailQuery(
      long dayStatusId,
      @NonNull LocalTime startTime,
      @NonNull LocalTime endTime,
      long ticketId,
      String ticketName,
      int ticketPrice,
      Long favoriteId
  ) {

    @QueryProjection
    public SwimmingPoolDetailFreeSwimmingDetailQuery {
    }

  }

}
