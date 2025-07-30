package com.project.swimcb.swimmingpool.adapter.out;

import static com.project.swimcb.db.entity.QFavoriteEntity.favoriteEntity;
import static com.project.swimcb.db.entity.QSwimmingClassEntity.swimmingClassEntity;
import static com.project.swimcb.db.entity.QSwimmingClassSubTypeEntity.swimmingClassSubTypeEntity;
import static com.project.swimcb.db.entity.QSwimmingClassTypeEntity.swimmingClassTypeEntity;
import static com.project.swimcb.db.entity.QTicketEntity.ticketEntity;
import static com.project.swimcb.db.entity.TicketTargetType.SWIMMING_CLASS;
import static com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName.GROUP;
import static com.querydsl.core.types.Projections.constructor;
import static java.time.DayOfWeek.FRIDAY;
import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.DayOfWeek.THURSDAY;
import static java.time.DayOfWeek.TUESDAY;
import static java.time.DayOfWeek.WEDNESDAY;
import static java.util.stream.Collectors.groupingBy;

import com.project.swimcb.favorite.domain.enums.FavoriteTargetType;
import com.project.swimcb.swimmingpool.adapter.in.FindSwimmingPoolDetailClassesCondition;
import com.project.swimcb.swimmingpool.adapter.in.FindSwimmingPoolDetailClassesResponse;
import com.project.swimcb.swimmingpool.adapter.in.FindSwimmingPoolDetailClassesResponse.SwimmingClass;
import com.project.swimcb.swimmingpool.adapter.in.FindSwimmingPoolDetailClassesResponse.SwimmingClassTicket;
import com.project.swimcb.swimmingpool.application.out.FindSwimmingPoolDetailClassesGateway;
import com.project.swimcb.swimmingpool.domain.SwimmingClassAvailabilityStatus;
import com.project.swimcb.swimmingpool.domain.enums.GroupFixedClassSubTypeName;
import com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.annotations.QueryProjection;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;
import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class FindSwimmingPoolDetailClassesDataMapper implements
    FindSwimmingPoolDetailClassesGateway {

  private final JPAQueryFactory queryFactory;

  @Override
  public FindSwimmingPoolDetailClassesResponse findSwimmingPoolDetailClasses(
      @NonNull FindSwimmingPoolDetailClassesCondition condition) {

    val swimmingClassDaysOfWeek = swimmingClassDaysOfWeek(condition.days());

    val swimmingClasses = queryFactory.select(constructor(QuerySwimmingPoolDetailClass.class,
            swimmingClassEntity.id,
            swimmingClassTypeEntity.name,
            swimmingClassSubTypeEntity.name,
            swimmingClassEntity.daysOfWeek,
            swimmingClassEntity.startTime,
            swimmingClassEntity.endTime,
            ticketEntity.price.min(),

            favoriteEntity.id.min(),

            swimmingClassEntity.reservationLimitCount,
            swimmingClassEntity.reservedCount,

            ticketEntity.id,
            ticketEntity.name,
            ticketEntity.price
        ))
        .from(swimmingClassEntity)
        .join(swimmingClassTypeEntity).on(swimmingClassEntity.type.eq(swimmingClassTypeEntity))
        .join(swimmingClassSubTypeEntity)
        .on(swimmingClassEntity.subType.eq(swimmingClassSubTypeEntity))
        .join(ticketEntity)
        .on(ticketEntity.targetId.eq(swimmingClassEntity.id))
        .leftJoin(favoriteEntity).on(favoriteJoinIfMemberIdExist(condition.memberId()))
        .where(
            swimmingClassEntity.swimmingPool.id.eq(condition.swimmingPoolId()),
            swimmingClassEntity.isVisible.isTrue(),
            swimmingClassEntity.isCanceled.isFalse(),
            swimmingClassEntity.year.between(condition.startDate().getYear(),
                condition.endDate().getYear()),
            swimmingClassEntity.month.between(condition.startDate().getMonthValue(),
                condition.endDate().getMonthValue()),
            classTimeBetweenStartTimes(condition.startTimes()),
            swimmingClassDaysOfWeek,
            classTypeAndSubTypeIn(condition.classTypes(), condition.classSubTypes()),

            ticketEntity.targetType.eq(SWIMMING_CLASS),
            ticketEntity.isDeleted.isFalse()
        )
        .groupBy(
            swimmingClassEntity.id,
            swimmingClassTypeEntity.name,
            swimmingClassSubTypeEntity.name,
            swimmingClassEntity.daysOfWeek,
            swimmingClassEntity.startTime,
            swimmingClassEntity.endTime,
            swimmingClassEntity.reservationLimitCount,
            swimmingClassEntity.reservedCount,
            ticketEntity.id,
            ticketEntity.name,
            ticketEntity.price
        )
        .offset(condition.pageable().getOffset())
        .limit(condition.pageable().getPageSize())
        .fetch();

    val classMap = swimmingClasses.stream()
        .collect(groupingBy(QuerySwimmingPoolDetailClass::swimmingClassId));
    val content = classMap
        .entrySet()
        .stream()
        .map(i -> {
          val value = i.getValue();
          return SwimmingClass.builder()
              .swimmingClassId(i.getKey())
              .type(value.getFirst().typeName().getDescription())
              .subType(value.getFirst().subTypeName())
              .days(bitVectorToDays(value.getFirst().daysOfWeek()))
              .startTime(value.getFirst().startTime())
              .endTime(value.getFirst().endTime())
              .minimumPrice(value.getFirst().minimumPrice())
              .favoriteId(value.getFirst().favoriteId())
              .isReservable(isReservable(value.getFirst().reservationLimitCount(),
                  value.getFirst().reservedCount()))
              .tickets(value.stream()
                  .map(j -> new SwimmingClassTicket(j.ticketId(), j.ticketName(), j.ticketPrice()))
                  .toList())
              .build();
        })
        .toList();

    val count = Optional.ofNullable(
            queryFactory.select(swimmingClassEntity.id.countDistinct())
                .from(swimmingClassEntity)
                .join(swimmingClassTypeEntity).on(swimmingClassEntity.type.eq(swimmingClassTypeEntity))
                .join(swimmingClassSubTypeEntity)
                .on(swimmingClassEntity.subType.eq(swimmingClassSubTypeEntity))
                .join(ticketEntity)
                .on(ticketEntity.targetId.eq(swimmingClassEntity.id))
                .where(
                    swimmingClassEntity.swimmingPool.id.eq(condition.swimmingPoolId()),
                    swimmingClassEntity.isVisible.isTrue(),
                    swimmingClassEntity.isCanceled.isFalse(),
                    swimmingClassEntity.year.between(condition.startDate().getYear(),
                        condition.endDate().getYear()),
                    swimmingClassEntity.month.between(condition.startDate().getMonthValue(),
                        condition.endDate().getMonthValue()),
                    classTimeBetweenStartTimes(condition.startTimes()),
                    swimmingClassDaysOfWeek,
                    classTypeAndSubTypeIn(condition.classTypes(), condition.classSubTypes()),

                    ticketEntity.targetType.eq(SWIMMING_CLASS),
                    ticketEntity.isDeleted.isFalse()
                )
                .fetchOne())
        .orElse(0L);

    val paged = new PageImpl<>(content, condition.pageable(), count);
    return new FindSwimmingPoolDetailClassesResponse(paged);
  }

  BooleanExpression favoriteJoinIfMemberIdExist(Long memberId) {
    if (memberId == null) {
      return Expressions.FALSE;
    }
    return favoriteEntity.member.id.eq(memberId)
        .and(favoriteEntity.targetId.eq(swimmingClassEntity.id))
        .and(favoriteEntity.targetType.eq(FavoriteTargetType.SWIMMING_CLASS));
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
      val subTypeNames = groupFixedClassSubTypeNames.stream().map(
          GroupFixedClassSubTypeName::getDescription).toList();
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

  List<String> bitVectorToDays(int bitVector) {
    val dayMap = Map.of(
        MONDAY, "월",
        TUESDAY, "화",
        WEDNESDAY, "수",
        THURSDAY, "목",
        FRIDAY, "금",
        SATURDAY, "토",
        SUNDAY, "일"
    );

    return IntStream.range(0, 7)
        .filter(i -> (bitVector & (1 << (6 - i))) > 0)
        .mapToObj(i -> dayMap.get(DayOfWeek.of(i + 1)))
        .toList();
  }

  private boolean isReservable(int reservationLimitCount, int reservedCount) {
    val availabilityStatus = SwimmingClassAvailabilityStatus.calculateStatus(
        reservationLimitCount, reservedCount);
    return availabilityStatus != SwimmingClassAvailabilityStatus.NOT_RESERVABLE;
  }

  @Builder
  public record QuerySwimmingPoolDetailClass(
      long swimmingClassId,
      @NonNull SwimmingClassTypeName typeName,
      @NonNull String subTypeName,
      int daysOfWeek,
      @NonNull LocalTime startTime,
      @NonNull LocalTime endTime,
      int minimumPrice,
      Long favoriteId,
      int reservationLimitCount,
      int reservedCount,
      long ticketId,
      String ticketName,
      int ticketPrice
  ) {

    @QueryProjection
    public QuerySwimmingPoolDetailClass {
    }

  }

}
