package com.project.swimcb.swimmingpool.adapter.out;

import static com.project.swimcb.db.entity.QFavoriteEntity.favoriteEntity;
import static com.project.swimcb.db.entity.QSwimmingClassEntity.swimmingClassEntity;
import static com.project.swimcb.db.entity.QSwimmingClassSubTypeEntity.swimmingClassSubTypeEntity;
import static com.project.swimcb.db.entity.QSwimmingClassTicketEntity.swimmingClassTicketEntity;
import static com.project.swimcb.db.entity.QSwimmingClassTypeEntity.swimmingClassTypeEntity;
import static com.project.swimcb.db.entity.QSwimmingPoolEntity.swimmingPoolEntity;
import static com.project.swimcb.db.entity.QSwimmingPoolImageEntity.swimmingPoolImageEntity;
import static com.project.swimcb.db.entity.QSwimmingPoolRatingEntity.swimmingPoolRatingEntity;
import static com.project.swimcb.db.entity.QSwimmingPoolReviewEntity.swimmingPoolReviewEntity;
import static com.project.swimcb.favorite.domain.enums.FavoriteTargetType.SWIMMING_POOL;
import static com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName.GROUP;
import static com.querydsl.core.types.Projections.constructor;

import com.project.swimcb.swimmingpool.adapter.in.FindSwimmingClassesResponse;
import com.project.swimcb.swimmingpool.adapter.in.FindSwimmingClassesResponse.SwimmingClass;
import com.project.swimcb.swimmingpool.application.out.FindSwimmingClassesDsGateway;
import com.project.swimcb.swimmingpool.domain.FindSwimmingClassesCondition;
import com.project.swimcb.swimmingpool.domain.enums.GroupFixedClassSubTypeName;
import com.project.swimcb.swimmingpool.domain.enums.Sort;
import com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.annotations.QueryProjection;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindSwimmingClassesDataMapper implements FindSwimmingClassesDsGateway {

  private final JPAQueryFactory queryFactory;

  @Override
  public FindSwimmingClassesResponse findSwimmingClasses(
      @NonNull FindSwimmingClassesCondition condition) {

    val distanceBetweenMemberAndSwimmingPool = distanceBetweenMemberAndSwimmingPool(
        condition.memberLatitude(), condition.memberLongitude());
    val swimmingClassDaysOfWeek = swimmingClassDaysOfWeek(condition.days());

    val swimmingClasses = queryFactory.select(constructor(SwimmingPoolWithClass.class,
            swimmingPoolEntity.id,
            swimmingPoolImageEntity.path,
            favoriteEntity.id.count().gt(0),
            distanceBetweenMemberAndSwimmingPool,
            swimmingPoolEntity.name,
            swimmingPoolEntity.address,
            swimmingPoolRatingEntity.rating.avg(),
            swimmingPoolReviewEntity.id.sum()
        ))
        .from(swimmingPoolEntity)
        .join(swimmingPoolImageEntity)
        .on(swimmingPoolImageEntity.swimmingPool.eq(swimmingPoolEntity))
        .join(swimmingClassEntity).on(swimmingClassEntity.swimmingPool.eq(swimmingPoolEntity))
        .join(swimmingClassTypeEntity).on(swimmingClassEntity.type.eq(swimmingClassTypeEntity))
        .join(swimmingClassSubTypeEntity)
        .on(swimmingClassEntity.subType.eq(swimmingClassSubTypeEntity))
        .join(swimmingClassTicketEntity)
        .on(swimmingClassTicketEntity.swimmingClass.eq(swimmingClassEntity))
        .leftJoin(favoriteEntity).on(favoriteJoinIfMemberIdExist(condition.memberId()))
        .leftJoin(swimmingPoolRatingEntity)
        .on(swimmingPoolRatingEntity.swimmingPool.eq(swimmingPoolEntity))
        .leftJoin(swimmingPoolReviewEntity)
        .on(swimmingPoolReviewEntity.swimmingPool.eq(swimmingPoolEntity))
        .where(
            swimmingPoolEntity.name.isNotNull(),
            swimmingPoolEntity.address.isNotNull(),
            // TODO. 폰번호 조건 추가
            swimmingPoolEntity.latitude.isNotNull(),
            swimmingPoolEntity.longitude.isNotNull(),
            swimmingPoolNameAndAddressContains(condition.keyword()),

            swimmingClassEntity.isVisible.isTrue(),
            swimmingClassEntity.isCanceled.isFalse(),
            swimmingClassEntity.year.between(condition.startDate().getYear(),
                condition.endDate().getYear()),
            swimmingClassEntity.month.between(condition.startDate().getMonthValue(),
                condition.endDate().getMonthValue()),
            classTimeBetweenStartTimes(condition.startTimes()),
            swimmingClassDaysOfWeek,
            classTypeAndSubTypeIn(condition.classTypes(), condition.classSubTypes()),

            swimmingClassTicketEntity.isDeleted.isFalse()
        )
        .groupBy(
            swimmingPoolEntity.id,
            swimmingPoolImageEntity.path,
            swimmingPoolEntity.longitude,
            swimmingPoolEntity.latitude,
            swimmingPoolEntity.name,
            swimmingPoolEntity.address
        )
        .orderBy(sort(condition.sort(), distanceBetweenMemberAndSwimmingPool))
        .offset(condition.pageable().getOffset())
        .limit(condition.pageable().getPageSize())
        .fetch()
        .stream()
        .map(i -> SwimmingClass.builder()
            .swimmingPoolId(i.swimmingPoolId())
            .imageUrl(i.imageUrl())
            .isFavorite(i.isFavorite())
            .distance((int) i.distance())
            .name(i.name())
            .address(i.address())
            .rating(i.rating())
            .reviewCount((int) i.reviewCount())
            .build()
        )
        .toList();

    val count = Optional.ofNullable(
            queryFactory.select(swimmingPoolEntity.id.countDistinct())
                .from(swimmingPoolEntity)
                .join(swimmingPoolImageEntity)
                .on(swimmingPoolImageEntity.swimmingPool.eq(swimmingPoolEntity))
                .join(swimmingClassEntity).on(swimmingClassEntity.swimmingPool.eq(swimmingPoolEntity))
                .join(swimmingClassTypeEntity).on(swimmingClassEntity.type.eq(swimmingClassTypeEntity))
                .join(swimmingClassSubTypeEntity)
                .on(swimmingClassEntity.subType.eq(swimmingClassSubTypeEntity))
                .join(swimmingClassTicketEntity)
                .on(swimmingClassTicketEntity.swimmingClass.eq(swimmingClassEntity))
                .where(
                    swimmingPoolEntity.name.isNotNull(),
                    swimmingPoolEntity.address.isNotNull(),
                    swimmingPoolEntity.latitude.isNotNull(),
                    swimmingPoolEntity.longitude.isNotNull(),
                    swimmingClassEntity.isVisible.eq(true),

                    swimmingPoolNameAndAddressContains(condition.keyword()),

                    swimmingClassEntity.year.between(condition.startDate().getYear(),
                        condition.endDate().getYear()),
                    swimmingClassEntity.month.between(condition.startDate().getMonthValue(),
                        condition.endDate().getMonthValue()),
                    classTimeBetweenStartTimes(condition.startTimes()),
                    swimmingClassDaysOfWeek,
                    classTypeAndSubTypeIn(condition.classTypes(), condition.classSubTypes())
                )
                .fetchOne())
        .orElse(0L);

    val paged = new PageImpl<>(swimmingClasses, condition.pageable(), count);
    return new FindSwimmingClassesResponse(paged);
  }

  NumberExpression<Double> distanceBetweenMemberAndSwimmingPool(double memberLatitude,
      double memberLongitude) {
    return Expressions.numberTemplate(Double.class,
        "(6371 * acos(cos(radians({0})) * cos(radians({1})) * " +
            "cos(radians({2}) - radians({3})) + sin(radians({0})) * sin(radians({1}))))",
        memberLatitude, swimmingPoolEntity.latitude, swimmingPoolEntity.longitude, memberLongitude);
  }

  BooleanExpression favoriteJoinIfMemberIdExist(Long memberId) {
    if (memberId == null) {
      return Expressions.FALSE;
    }
    return favoriteEntity.member.id.eq(memberId)
        .and(favoriteEntity.targetId.eq(swimmingPoolEntity.id))
        .and(favoriteEntity.targetType.eq(SWIMMING_POOL));
  }

  Predicate swimmingPoolNameAndAddressContains(String keyword) {
    if (keyword == null) {
      return null;
    }
    return swimmingPoolEntity.name.containsIgnoreCase(keyword)
        .or(swimmingPoolEntity.address.containsIgnoreCase(keyword));
  }

  BooleanBuilder classTimeBetweenStartTimes(@NonNull List<LocalTime> startTimes) {
    if (startTimes.isEmpty()) {
      return null;
    }
    val builder = new BooleanBuilder();

    startTimes.forEach(i -> {
      val endTime = i.plusHours(1);
      builder.or(
          swimmingClassEntity.startTime.goe(i).and(swimmingClassEntity.startTime.lt(endTime)));
    });

    return builder;
  }

  OrderSpecifier<?> sort(@NonNull Sort sort, @NonNull NumberExpression<Double> distanceExpression) {
    if (sort == Sort.DISTANCE_ASC) {
      return distanceExpression.asc();
    }
    return swimmingClassTicketEntity.price.asc();
  }

  Predicate classTypeAndSubTypeIn(
      @NonNull List<SwimmingClassTypeName> swimmingClassTypeNames,
      @NonNull List<GroupFixedClassSubTypeName> groupFixedClassSubTypeNames) {

    if (swimmingClassTypeNames.isEmpty() && groupFixedClassSubTypeNames.isEmpty()) {
      return null;
    }

    if (swimmingClassTypeNames.isEmpty()) {
      return Expressions.asBoolean(false);
    }

    val booleanBuilder = new BooleanBuilder();

    if (!groupFixedClassSubTypeNames.isEmpty() && swimmingClassTypeNames.contains(GROUP)) {
      val subTypeNames = groupFixedClassSubTypeNames.stream().map(Enum::name).toList();
      booleanBuilder.or(
          swimmingClassTypeEntity.name.eq(GROUP)
              .and(swimmingClassSubTypeEntity.name.in(subTypeNames)));
    }

    val classTypesExceptGroup = swimmingClassTypeNames.stream().filter(i -> i != GROUP).toList();
    booleanBuilder.or(swimmingClassTypeEntity.name.in(classTypesExceptGroup));

    return booleanBuilder;
  }

  BooleanExpression swimmingClassDaysOfWeek(@NonNull List<DayOfWeek> days) {
    if (days.isEmpty()) {
      return null;
    }
    val dayBitVector = daysToBitVector(days);
    return Expressions.numberTemplate(Integer.class, "bitand({0}, {1})",
        swimmingClassEntity.daysOfWeek, dayBitVector).gt(0);
  }

  int daysToBitVector(@NonNull List<DayOfWeek> days) {
    return days.stream().map(i -> 1 << (6 - (i.getValue() - 1))).reduce(0, Integer::sum);
  }

  @Builder
  public record SwimmingPoolWithClass(
      long swimmingPoolId,
      @NonNull String imageUrl,
      boolean isFavorite,
      double distance,
      @NonNull String name,
      @NonNull String address,
      double rating,
      long reviewCount
  ) {

    @QueryProjection
    public SwimmingPoolWithClass {
    }

  }

}
