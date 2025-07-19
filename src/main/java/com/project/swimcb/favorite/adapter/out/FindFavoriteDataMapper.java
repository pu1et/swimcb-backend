package com.project.swimcb.favorite.adapter.out;

import static com.project.swimcb.db.entity.QFavoriteEntity.favoriteEntity;
import static com.project.swimcb.db.entity.QFreeSwimmingDayStatusEntity.freeSwimmingDayStatusEntity;
import static com.project.swimcb.db.entity.QFreeSwimmingEntity.freeSwimmingEntity;
import static com.project.swimcb.db.entity.QSwimmingClassEntity.swimmingClassEntity;
import static com.project.swimcb.db.entity.QSwimmingClassSubTypeEntity.swimmingClassSubTypeEntity;
import static com.project.swimcb.db.entity.QSwimmingClassTypeEntity.swimmingClassTypeEntity;
import static com.project.swimcb.db.entity.QSwimmingPoolEntity.swimmingPoolEntity;
import static com.project.swimcb.db.entity.QSwimmingPoolImageEntity.swimmingPoolImageEntity;
import static com.project.swimcb.db.entity.QSwimmingPoolRatingEntity.swimmingPoolRatingEntity;
import static com.project.swimcb.db.entity.QSwimmingPoolReviewEntity.swimmingPoolReviewEntity;
import static com.project.swimcb.db.entity.TicketTargetType.FREE_SWIMMING;
import static com.project.swimcb.db.entity.TicketTargetType.SWIMMING_CLASS;
import static com.querydsl.core.types.Projections.constructor;

import com.project.swimcb.db.entity.QTicketEntity;
import com.project.swimcb.favorite.application.out.FindFavoriteDsGateway;
import com.project.swimcb.favorite.domain.Favorite;
import com.project.swimcb.favorite.domain.FindFavoriteCondition;
import com.project.swimcb.favorite.domain.FreeSwimmingFavorite;
import com.project.swimcb.favorite.domain.SwimmingClassFavorite;
import com.project.swimcb.favorite.domain.SwimmingPoolFavorite;
import com.project.swimcb.favorite.domain.enums.FavoriteTargetType;
import com.project.swimcb.mypage.reservation.adapter.out.ClassDayOfWeek;
import com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.annotations.QueryProjection;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class FindFavoriteDataMapper implements FindFavoriteDsGateway {

  private final JPAQueryFactory queryFactory;

  @Override
  public Page<Favorite> findFavorites(@NonNull FindFavoriteCondition condition) {

    val distanceBetweenMemberAndSwimmingPool = distanceBetweenMemberAndSwimmingPool(
        condition.memberLatitude(), condition.memberLongitude());

    val swimmingClassTicketEntity = new QTicketEntity("swimmingClassTicketEntity");
    val freeSwimmingTicketEntity = new QTicketEntity("freeSwimmingTicketEntity");

    val contents = queryFactory.select(constructor(QueryFavorite.class,
            favoriteEntity.id,
            favoriteEntity.targetId,
            favoriteEntity.targetType,

            swimmingPoolImageEntity.path,
            distanceBetweenMemberAndSwimmingPool,
            swimmingPoolEntity.name,
            swimmingPoolEntity.address,
            swimmingPoolRatingEntity.rating.avg(),
            swimmingPoolReviewEntity.id.sum(),

            swimmingClassEntity.month,
            swimmingClassTypeEntity.name,
            swimmingClassSubTypeEntity.name,
            swimmingClassEntity.daysOfWeek,
            swimmingClassEntity.startTime,
            swimmingClassEntity.endTime,
            swimmingClassTicketEntity.price.min(),

            freeSwimmingEntity.yearMonth,
            freeSwimmingDayStatusEntity.dayOfMonth,
            freeSwimmingEntity.startTime,
            freeSwimmingEntity.endTime,
            freeSwimmingTicketEntity.price.min()
        ))
        .from(favoriteEntity)

        .leftJoin(swimmingClassEntity)
        .on(
            favoriteEntity.targetType.eq(FavoriteTargetType.SWIMMING_CLASS),
            swimmingClassEntity.id.eq(favoriteEntity.targetId)
        )

        .leftJoin(swimmingClassTypeEntity)
        .on(swimmingClassEntity.type.eq(swimmingClassTypeEntity))

        .leftJoin(swimmingClassSubTypeEntity)
        .on(swimmingClassEntity.subType.eq(swimmingClassSubTypeEntity))

        .leftJoin(freeSwimmingDayStatusEntity)
        .on(
            favoriteEntity.targetType.eq(FavoriteTargetType.FREE_SWIMMING),
            freeSwimmingDayStatusEntity.id.eq(favoriteEntity.targetId)
        )

        .leftJoin(freeSwimmingEntity)
        .on(freeSwimmingEntity.eq(freeSwimmingDayStatusEntity.freeSwimming))

        .leftJoin(swimmingPoolEntity)
        .on(
            swimmingPoolJoinCondition(),
            swimmingPoolEntity.name.isNotNull(),
            swimmingPoolEntity.address.isNotNull(),
            swimmingPoolEntity.phone.isNotNull(),
            swimmingPoolEntity.latitude.isNotNull(),
            swimmingPoolEntity.longitude.isNotNull(),
            swimmingPoolEntity.latitude.isNotNull(),
            swimmingPoolEntity.longitude.isNotNull()
        )

        .leftJoin(swimmingPoolImageEntity)
        .on(swimmingPoolImageEntity.swimmingPool.eq(swimmingPoolEntity)).limit(1)

        .leftJoin(swimmingPoolRatingEntity)
        .on(swimmingPoolRatingEntity.swimmingPool.eq(swimmingPoolEntity))

        .leftJoin(swimmingPoolReviewEntity)
        .on(swimmingPoolReviewEntity.swimmingPool.eq(swimmingPoolEntity))

        .leftJoin(swimmingClassTicketEntity)
        .on(
            swimmingClassTicketEntity.targetType.eq(SWIMMING_CLASS),
            swimmingClassTicketEntity.targetId.eq(swimmingClassEntity.id),
            swimmingClassTicketEntity.isDeleted.isFalse()
        )

        .leftJoin(freeSwimmingTicketEntity)
        .on(
            freeSwimmingTicketEntity.targetType.eq(FREE_SWIMMING),
            freeSwimmingTicketEntity.targetId.eq(freeSwimmingEntity.id),
            freeSwimmingTicketEntity.isDeleted.isFalse()
        )
        .where(
            favoriteEntity.member.id.eq(condition.memberId()),
            favoriteTargetTypeEq(condition.targetType())
        )
        .orderBy(favoriteEntity.createdAt.desc())
        .groupBy(
            favoriteEntity.id,
            favoriteEntity.targetId,
            favoriteEntity.targetType,

            swimmingPoolImageEntity.path,
            swimmingPoolEntity.latitude,
            swimmingPoolEntity.longitude,
            swimmingPoolEntity.name,
            swimmingPoolEntity.address,

            swimmingClassEntity.month,
            swimmingClassTypeEntity.name,
            swimmingClassSubTypeEntity.name,
            swimmingClassEntity.daysOfWeek,
            swimmingClassEntity.startTime,
            swimmingClassEntity.endTime,

            freeSwimmingEntity.yearMonth,
            freeSwimmingDayStatusEntity.dayOfMonth,
            freeSwimmingEntity.startTime,
            freeSwimmingEntity.endTime
        )
        .offset(condition.pageable().getOffset())
        .limit(condition.pageable().getPageSize())
        .fetch()
        .stream()
        .map(i -> switch (i.targetType) {
          case SWIMMING_POOL -> mapToSwimmingPool(i);
          case SWIMMING_CLASS -> mapToSwimmingClass(i);
          case FREE_SWIMMING -> mapToFreeSwimming(i);
        })
        .toList();

    val count = queryFactory.select(favoriteEntity.id.count())
        .from(favoriteEntity)

        .leftJoin(swimmingClassEntity)
        .on(
            favoriteEntity.targetType.eq(FavoriteTargetType.SWIMMING_CLASS),
            swimmingClassEntity.id.eq(favoriteEntity.targetId)
        )

        .leftJoin(freeSwimmingDayStatusEntity)
        .on(
            favoriteEntity.targetType.eq(FavoriteTargetType.FREE_SWIMMING),
            freeSwimmingDayStatusEntity.id.eq(favoriteEntity.targetId)
        )

        .leftJoin(freeSwimmingEntity)
        .on(freeSwimmingEntity.eq(freeSwimmingDayStatusEntity.freeSwimming))

        .leftJoin(swimmingPoolEntity)
        .on(
            swimmingPoolJoinCondition(),
            swimmingPoolEntity.name.isNotNull(),
            swimmingPoolEntity.address.isNotNull(),
            swimmingPoolEntity.phone.isNotNull(),
            swimmingPoolEntity.latitude.isNotNull(),
            swimmingPoolEntity.longitude.isNotNull(),
            swimmingPoolEntity.latitude.isNotNull(),
            swimmingPoolEntity.longitude.isNotNull()
        )

        .where(
            favoriteEntity.member.id.eq(condition.memberId())
        )
        .fetchOne();

    return new PageImpl<>(contents, condition.pageable(), Optional.ofNullable(count).orElse(0L));
  }

  private BooleanExpression favoriteTargetTypeEq(FavoriteTargetType favoriteTargetType) {
    if (favoriteTargetType == null) {
      return null;
    }
    return favoriteEntity.targetType.eq(favoriteTargetType);
  }

  private BooleanBuilder swimmingPoolJoinCondition() {
    val booleanBuilder = new BooleanBuilder();

    booleanBuilder.or(
        favoriteEntity.targetType.eq(FavoriteTargetType.SWIMMING_POOL)
            .and(swimmingPoolEntity.id.eq(favoriteEntity.targetId))
    );

    booleanBuilder.or(
        favoriteEntity.targetType.eq(FavoriteTargetType.SWIMMING_CLASS)
            .and(swimmingPoolEntity.eq(swimmingClassEntity.swimmingPool))
    );

    booleanBuilder.or(
        favoriteEntity.targetType.eq(FavoriteTargetType.FREE_SWIMMING)
            .and(swimmingPoolEntity.eq(freeSwimmingEntity.swimmingPool))
    );

    return booleanBuilder;
  }

  private Favorite mapToSwimmingPool(@NonNull QueryFavorite i) {
    return SwimmingPoolFavorite.builder()
        .targetId(i.targetId())
        .targetType(i.targetType())
        .imagePath(i.imagePath())
        .distance(i.distance().intValue())
        .name(i.name())
        .address(i.address())
        .rating(i.rating())
        .reviewCount(i.reviewCount())
        .build();
  }

  private Favorite mapToSwimmingClass(@NonNull QueryFavorite i) {
    return SwimmingClassFavorite.builder()
        .targetId(i.targetId())
        .targetType(i.targetType())
        .swimmingPoolName(i.name())
        .month(i.month())
        .type(i.type())
        .subType(i.subType())
        .daysOfWeek(ClassDayOfWeek.of(i.daysOfWeek()))
        .startTime(i.swimmingClassStartTime())
        .endTime(i.swimmingClassEndTime())
        .minTicketPrice(i.swimmingClassMinTicketPrice())
        .build();
  }

  private Favorite mapToFreeSwimming(@NonNull QueryFavorite i) {
    return FreeSwimmingFavorite.builder()
        .targetId(i.targetId())
        .targetType(i.targetType())
        .swimmingPoolName(i.name())
        .date(LocalDate.of(i.yearMonth().getYear(), i.yearMonth().getMonth(), i.dayOfMonth()))
        .startTime(i.freeSwimmingStartTime())
        .endTime(i.freeSwimmingEndTime())
        .minTicketPrice(i.freeSwimmingMinTicketPrice())
        .build();
  }

  private NumberExpression<Double> distanceBetweenMemberAndSwimmingPool(
      @NonNull Double memberLatitude,
      @NonNull Double memberLongitude
  ) {
    return Expressions.numberTemplate(Double.class,
        "(6371000 * acos(cos(radians({0})) * cos(radians({1})) * " +
            "cos(radians({2}) - radians({3})) + sin(radians({0})) * sin(radians({1}))))",
        memberLatitude, swimmingPoolEntity.latitude, swimmingPoolEntity.longitude,
        memberLongitude);
  }


  public record QueryFavorite(
      long id,
      long targetId,
      @NonNull FavoriteTargetType targetType,

      String imagePath,
      Double distance,
      String name,
      String address,
      Double rating,
      Long reviewCount,

      Integer month,
      SwimmingClassTypeName type,
      String subType,
      Integer daysOfWeek,
      LocalTime swimmingClassStartTime,
      LocalTime swimmingClassEndTime,
      Integer swimmingClassMinTicketPrice,

      LocalDate yearMonth,
      Integer dayOfMonth,
      LocalTime freeSwimmingStartTime,
      LocalTime freeSwimmingEndTime,
      Integer freeSwimmingMinTicketPrice
  ) {

    @QueryProjection
    public QueryFavorite {
    }

  }

}
