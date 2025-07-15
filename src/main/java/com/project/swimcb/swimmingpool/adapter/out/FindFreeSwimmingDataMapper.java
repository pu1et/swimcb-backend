package com.project.swimcb.swimmingpool.adapter.out;

import static com.project.swimcb.db.entity.QFavoriteEntity.favoriteEntity;
import static com.project.swimcb.db.entity.QFreeSwimmingDayStatusEntity.freeSwimmingDayStatusEntity;
import static com.project.swimcb.db.entity.QFreeSwimmingEntity.freeSwimmingEntity;
import static com.project.swimcb.db.entity.QSwimmingPoolEntity.swimmingPoolEntity;
import static com.project.swimcb.db.entity.QSwimmingPoolImageEntity.swimmingPoolImageEntity;
import static com.project.swimcb.db.entity.QSwimmingPoolRatingEntity.swimmingPoolRatingEntity;
import static com.project.swimcb.db.entity.QSwimmingPoolReviewEntity.swimmingPoolReviewEntity;
import static com.project.swimcb.db.entity.QTicketEntity.ticketEntity;
import static com.project.swimcb.db.entity.TicketTargetType.FREE_SWIMMING;
import static com.querydsl.core.types.Projections.constructor;

import com.project.swimcb.favorite.domain.enums.FavoriteTargetType;
import com.project.swimcb.swimmingpool.application.in.FindFreeSwimmingCondition;
import com.project.swimcb.swimmingpool.application.out.FindFreeSwimmingDsGateway;
import com.project.swimcb.swimmingpool.domain.FreeSwimming;
import com.querydsl.core.annotations.QueryProjection;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class FindFreeSwimmingDataMapper implements FindFreeSwimmingDsGateway {

  private final JPAQueryFactory queryFactory;

  @Override
  public List<FreeSwimming> findFreeSwimming(
      @NonNull FindFreeSwimmingCondition condition) {

    val distanceBetweenMemberAndSwimmingPool = distanceBetweenMemberAndSwimmingPool(
        condition.memberLatitude(), condition.memberLongitude());

    return queryFactory.select(constructor(SwimmingPoolWithFreeSwimming.class,
            swimmingPoolEntity.id,
            swimmingPoolImageEntity.path,
            favoriteEntity.id.count().gt(0),
            distanceBetweenMemberAndSwimmingPool,
            swimmingPoolEntity.name,
            swimmingPoolEntity.address,
            swimmingPoolRatingEntity.rating.avg(),
            swimmingPoolReviewEntity.id.sum(),
            swimmingPoolEntity.latitude,
            swimmingPoolEntity.longitude
        ))
        .from(swimmingPoolEntity)
        .join(swimmingPoolImageEntity)
        .on(swimmingPoolImageEntity.swimmingPool.eq(swimmingPoolEntity))

        .join(freeSwimmingEntity)
        .on(freeSwimmingEntity.swimmingPool.eq(swimmingPoolEntity))

        .join(ticketEntity)
        .on(ticketEntity.targetId.eq(freeSwimmingEntity.id))

        .join(freeSwimmingDayStatusEntity)
        .on(freeSwimmingDayStatusEntity.freeSwimming.eq(freeSwimmingEntity))

        .leftJoin(favoriteEntity)
        .on(favoriteJoinIfMemberIdExist(condition.memberId()))

        .leftJoin(swimmingPoolRatingEntity)
        .on(swimmingPoolRatingEntity.swimmingPool.eq(swimmingPoolEntity))

        .leftJoin(swimmingPoolReviewEntity)
        .on(swimmingPoolReviewEntity.swimmingPool.eq(swimmingPoolEntity))

        .where(
            swimmingPoolEntity.name.isNotNull(),
            swimmingPoolEntity.address.isNotNull(),
            swimmingPoolEntity.phone.isNotNull(),

            swimmingPoolEntity.latitude.between(condition.minLatitude(), condition.maxLatitude()),
            swimmingPoolEntity.longitude.between(condition.minLongitude(),
                condition.maxLongitude()),

            freeSwimmingEntity.isVisible.isTrue(),
            freeSwimmingEntity.isCanceled.isFalse(),

            filterDateAndTimes(condition.isTodayAvailable(), condition.date()),

            ticketEntity.targetType.eq(FREE_SWIMMING),
            ticketEntity.isDeleted.isFalse()

        )
        .groupBy(
            swimmingPoolEntity.id,
            swimmingPoolImageEntity.path,
            swimmingPoolEntity.longitude,
            swimmingPoolEntity.latitude,
            swimmingPoolEntity.name,
            swimmingPoolEntity.address,
            swimmingPoolEntity.latitude,
            swimmingPoolEntity.longitude
        )
        .fetch()
        .stream()
        .map(i -> FreeSwimming.builder()
            .swimmingPoolId(i.swimmingPoolId())
            .imagePath(i.imageUrl())
            .isFavorite(i.isFavorite())
            .distance((int) i.distance())
            .name(i.name())
            .address(i.address())
            .rating(i.rating())
            .reviewCount((int) i.reviewCount())
            .latitude(i.latitude())
            .longitude(i.longitude())
            .build())
        .toList();
  }

  private NumberExpression<Double> distanceBetweenMemberAndSwimmingPool(double memberLatitude,
      double memberLongitude) {
    return Expressions.numberTemplate(Double.class,
        "(6371 * acos(cos(radians({0})) * cos(radians({1})) * " +
            "cos(radians({2}) - radians({3})) + sin(radians({0})) * sin(radians({1}))))",
        memberLatitude, swimmingPoolEntity.latitude, swimmingPoolEntity.longitude, memberLongitude);
  }

  private BooleanExpression favoriteJoinIfMemberIdExist(Long memberId) {
    if (memberId == null) {
      return Expressions.FALSE;
    }
    return favoriteEntity.member.id.eq(memberId)
        .and(favoriteEntity.targetId.eq(swimmingPoolEntity.id))
        .and(favoriteEntity.targetType.eq(FavoriteTargetType.FREE_SWIMMING));
  }

  private BooleanExpression filterDateAndTimes(
      @NonNull Boolean isTodayAvailable,
      LocalDate date
  ) {

    val now = LocalDate.now();
    if (isTodayAvailable) {
      val firstDayOfMonth = now.withDayOfMonth(1);
      freeSwimmingEntity.yearMonth.eq(firstDayOfMonth)
          .and(freeSwimmingDayStatusEntity.dayOfMonth.eq(now.getDayOfMonth()));
    }

    if (date != null) {
      val firstDayOfMonth = date.withDayOfMonth(1);
      return freeSwimmingEntity.yearMonth.eq(firstDayOfMonth)
          .and(freeSwimmingDayStatusEntity.dayOfMonth.eq(date.getDayOfMonth()));
    }
    return null;
  }

  @Builder
  protected record SwimmingPoolWithFreeSwimming(
      long swimmingPoolId,
      @NonNull String imageUrl,
      boolean isFavorite,
      double distance,
      @NonNull String name,
      @NonNull String address,
      double rating,
      long reviewCount,
      double latitude,
      double longitude
  ) {

    @QueryProjection
    public SwimmingPoolWithFreeSwimming {

    }

  }

}
