package com.project.swimcb.swimmingpool.adapter.out;

import static com.project.swimcb.bo.swimmingclass.domain.QSwimmingClass.swimmingClass;
import static com.project.swimcb.bo.swimmingclass.domain.QSwimmingClassSubType.swimmingClassSubType;
import static com.project.swimcb.bo.swimmingclass.domain.QSwimmingClassTicket.swimmingClassTicket;
import static com.project.swimcb.bo.swimmingclass.domain.QSwimmingClassType.swimmingClassType;
import static com.project.swimcb.bo.swimmingpool.domain.QSwimmingPool.swimmingPool;
import static com.project.swimcb.bo.swimmingpool.domain.QSwimmingPoolImage.swimmingPoolImage;
import static com.project.swimcb.favorite.domain.QFavorite.favorite;
import static com.project.swimcb.favorite.domain.enums.FavoriteTargetType.SWIMMING_POOL;
import static com.project.swimcb.swimming_pool_rating.application.in.QSwimmingPoolRating.swimmingPoolRating;
import static com.project.swimcb.swimming_pool_review.domain.QSwimmingPoolReview.swimmingPoolReview;
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
            swimmingPool.id,
            swimmingPoolImage.path,
            favorite.id.count().gt(0),
            distanceBetweenMemberAndSwimmingPool,
            swimmingPool.name,
            swimmingPool.address,
            swimmingPoolRating.rating.avg(),
            swimmingPoolReview.id.sum()
        ))
        .from(swimmingPool)
        .join(swimmingPoolImage).on(swimmingPoolImage.swimmingPool.eq(swimmingPool))
        .join(swimmingClass).on(swimmingClass.swimmingPool.eq(swimmingPool))
        .join(swimmingClassType).on(swimmingClass.type.eq(swimmingClassType))
        .join(swimmingClassSubType).on(swimmingClass.subType.eq(swimmingClassSubType))
        .join(swimmingClassTicket).on(swimmingClassTicket.swimmingClass.eq(swimmingClass))
        .leftJoin(favorite).on(favoriteJoinIfMemberIdExist(condition.memberId()))
        .leftJoin(swimmingPoolRating).on(swimmingPoolRating.swimmingPool.eq(swimmingPool))
        .leftJoin(swimmingPoolReview).on(swimmingPoolReview.swimmingPool.eq(swimmingPool))
        .where(
            swimmingPool.name.isNotNull(),
            swimmingPool.address.isNotNull(),
            // TODO. 폰번호 조건 추가
            swimmingPool.latitude.isNotNull(),
            swimmingPool.longitude.isNotNull(),
            swimmingPoolNameAndAddressContains(condition.keyword()),

            swimmingClass.isVisible.isTrue(),
            swimmingClass.isCanceled.isFalse(),
            swimmingClass.year.between(condition.startDate().getYear(),
                condition.endDate().getYear()),
            swimmingClass.month.between(condition.startDate().getMonthValue(),
                condition.endDate().getMonthValue()),
            classTimeBetweenStartTimes(condition.startTimes()),
            swimmingClassDaysOfWeek,
            classTypeAndSubTypeIn(condition.classTypes(), condition.classSubTypes()),

            swimmingClassTicket.isDeleted.isFalse()
        )
        .groupBy(
            swimmingPool.id,
            swimmingPoolImage.path,
            swimmingPool.longitude,
            swimmingPool.latitude,
            swimmingPool.name,
            swimmingPool.address
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
            queryFactory.select(swimmingPool.id.countDistinct())
                .from(swimmingPool)
                .join(swimmingPoolImage).on(swimmingPoolImage.swimmingPool.eq(swimmingPool))
                .join(swimmingClass).on(swimmingClass.swimmingPool.eq(swimmingPool))
                .join(swimmingClassType).on(swimmingClass.type.eq(swimmingClassType))
                .join(swimmingClassSubType).on(swimmingClass.subType.eq(swimmingClassSubType))
                .join(swimmingClassTicket).on(swimmingClassTicket.swimmingClass.eq(swimmingClass))
                .where(
                    swimmingPool.name.isNotNull(),
                    swimmingPool.address.isNotNull(),
                    swimmingPool.latitude.isNotNull(),
                    swimmingPool.longitude.isNotNull(),
                    swimmingClass.isVisible.eq(true),

                    swimmingPoolNameAndAddressContains(condition.keyword()),

                    swimmingClass.year.between(condition.startDate().getYear(),
                        condition.endDate().getYear()),
                    swimmingClass.month.between(condition.startDate().getMonthValue(),
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
        memberLatitude, swimmingPool.latitude, swimmingPool.longitude, memberLongitude);
  }

  BooleanExpression favoriteJoinIfMemberIdExist(Long memberId) {
    if (memberId == null) {
      return Expressions.FALSE;
    }
    return favorite.member.id.eq(memberId)
        .and(favorite.targetId.eq(swimmingPool.id))
        .and(favorite.targetType.eq(SWIMMING_POOL));
  }

  Predicate swimmingPoolNameAndAddressContains(String keyword) {
    if (keyword == null) {
      return null;
    }
    return swimmingPool.name.containsIgnoreCase(keyword)
        .or(swimmingPool.address.containsIgnoreCase(keyword));
  }

  BooleanBuilder classTimeBetweenStartTimes(@NonNull List<LocalTime> startTimes) {
    if (startTimes.isEmpty()) {
      return null;
    }
    val builder = new BooleanBuilder();

    startTimes.forEach(i -> {
      val endTime = i.plusHours(1);
      builder.or(swimmingClass.startTime.goe(i).and(swimmingClass.startTime.lt(endTime)));
    });

    return builder;
  }

  OrderSpecifier<?> sort(@NonNull Sort sort, @NonNull NumberExpression<Double> distanceExpression) {
    if (sort == Sort.DISTANCE_ASC) {
      return distanceExpression.asc();
    }
    return swimmingClassTicket.price.asc();
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
          swimmingClassType.name.eq(GROUP).and(swimmingClassSubType.name.in(subTypeNames)));
    }

    val classTypesExceptGroup = swimmingClassTypeNames.stream().filter(i -> i != GROUP).toList();
    booleanBuilder.or(swimmingClassType.name.in(classTypesExceptGroup));

    return booleanBuilder;
  }

  BooleanExpression swimmingClassDaysOfWeek(@NonNull List<DayOfWeek> days) {
    if (days.isEmpty()) {
      return null;
    }
    val dayBitVector = daysToBitVector(days);
    return Expressions.numberTemplate(Integer.class, "bitand({0}, {1})",
        swimmingClass.daysOfWeek, dayBitVector).gt(0);
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
