package com.project.swimcb.swimmingpool.adapter.out;

import static com.project.swimcb.db.entity.QFavoriteEntity.favoriteEntity;
import static com.project.swimcb.db.entity.QSwimmingClassEntity.swimmingClassEntity;
import static com.project.swimcb.db.entity.QSwimmingClassSubTypeEntity.swimmingClassSubTypeEntity;
import static com.project.swimcb.db.entity.QSwimmingClassTypeEntity.swimmingClassTypeEntity;
import static com.project.swimcb.db.entity.QTicketEntity.ticketEntity;
import static com.project.swimcb.db.entity.TicketTargetType.SWIMMING_CLASS;
import static com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName.AQUA_AEROBICS;
import static com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName.GROUP;
import static com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName.KIDS_SWIMMING;
import static com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName.PRIVATE_LESSON;
import static com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName.SPECIAL_CLASS;
import static com.querydsl.core.types.Projections.constructor;
import static java.time.DayOfWeek.FRIDAY;
import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.DayOfWeek.THURSDAY;
import static java.time.DayOfWeek.TUESDAY;
import static java.time.DayOfWeek.WEDNESDAY;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

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
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.LinkedHashMap;
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

    // 1단계: swimmingClassId만 페이징해서 조회
    val pagedSwimmingClassIds = queryFactory
        .select(swimmingClassEntity.id)
        .from(swimmingClassEntity)
        .join(swimmingClassTypeEntity).on(swimmingClassEntity.type.eq(swimmingClassTypeEntity))
        .join(swimmingClassSubTypeEntity)
        .on(swimmingClassEntity.subType.eq(swimmingClassSubTypeEntity))
        .join(ticketEntity)
        .on(
            ticketEntity.targetId.eq(swimmingClassEntity.id)
                .and(ticketEntity.targetType.eq(SWIMMING_CLASS))
                .and(ticketEntity.isDeleted.isFalse())
        )
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
            classTypeAndSubTypeIn(condition.classTypes(), condition.classSubTypes())
        )
        .groupBy(
            swimmingClassEntity.id,
            swimmingClassTypeEntity.name,
            swimmingClassSubTypeEntity.name,
            swimmingClassEntity.daysOfWeek,
            swimmingClassEntity.startTime,
            swimmingClassEntity.endTime
        )
        .orderBy(
            /**
             * 클래스 정렬
             *
             * - 1순위 : 강습형태 순서 (단체강습>레슨>키즈>특별반>아쿠아로빅)
             * - 2순위 : 시작 시간 빠른 순서
             * - 3순위 : 강습구분 선수 [ 단체강습(기초>초급>초중급>중급>중상급>상급>연수>마스터즈>종합), 나머지 강습구분은 오름차순 ]
             * - 4순위 : 요일 빠른 순서 (월>화>수>목>금>토>일)
             * - 5순위 : 요일 개수가 적은 순서 Ex) 월, 월목, 월수금 의 강습이 존재할 경우 월>월목>월수금
             * - 6순위 : 가격 및 최저가격 낮은 순
             */
            // 1순위 : 강습형태 순서 (단체강습>레슨>키즈>특별반>아쿠아로빅)
            typeOrder().asc(),

            // 2순위 : 시작 시간 빠른 순서
            swimmingClassEntity.startTime.asc(),

            // 3순위 : 강습구분 선수 [ 단체강습(기초>초급>초중급>중급>중상급>상급>연수>마스터즈>종합), 나머지 강습구분은 오름차순 ]
            subTypeOrder().asc(),
            swimmingClassSubTypeEntity.name.asc(),

            // 4순위 : 요일 빠른 순서 (월>화>수>목>금>토>일)
            minDayOfWeekOrder().asc(),

            // 5순위 : 요일 개수가 적은 순서
            daysOfWeekCountOrder().asc(),

            // 6순위 : 최저가격 낮은 순
            ticketEntity.price.min().asc()
        )
        .offset(condition.pageable().getOffset())
        .limit(condition.pageable().getPageSize())
        .fetch();

    // 페이징된 ID가 없으면 빈 결과 반환
    if (pagedSwimmingClassIds.isEmpty()) {
      val count = 0L;
      val paged = new PageImpl<SwimmingClass>(List.of(), condition.pageable(), count);
      return new FindSwimmingPoolDetailClassesResponse(paged);
    }

    // 2단계: 페이징된 ID들로 상세 정보(티켓 포함) 조회
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
        .on(
            ticketEntity.targetId.eq(swimmingClassEntity.id)
                .and(ticketEntity.targetType.eq(SWIMMING_CLASS))
                .and(ticketEntity.isDeleted.isFalse())
        )
        .leftJoin(favoriteEntity).on(favoriteJoinIfMemberIdExist(condition.memberId()))
        .where(
            swimmingClassEntity.id.in(pagedSwimmingClassIds)
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
        .fetch();

    // swimmingClassId별로 그룹핑
    val classMap = swimmingClasses.stream()
        .collect(groupingBy(
            QuerySwimmingPoolDetailClass::swimmingClassId,
            LinkedHashMap::new,
            toList()
        ));

    // 1단계에서 조회한 순서대로 정렬
    val content = pagedSwimmingClassIds.stream()
        .map(classMap::get)
        .filter(value -> value != null && !value.isEmpty())
        .map(value -> {
          val first = value.get(0);
          val minPrice = value.stream().mapToInt(QuerySwimmingPoolDetailClass::minimumPrice).min()
              .orElse(0);
          return SwimmingClass.builder()
              .swimmingClassId(first.swimmingClassId())
              .type(first.typeName().getDescription())
              .subType(first.subTypeName())
              .days(bitVectorToDays(first.daysOfWeek()))
              .startTime(first.startTime())
              .endTime(first.endTime())
              .minimumPrice(minPrice)
              .favoriteId(first.favoriteId())
              .isReservable(isReservable(first.reservationLimitCount(),
                  first.reservedCount()))
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
                .on(
                    ticketEntity.targetId.eq(swimmingClassEntity.id)
                        .and(ticketEntity.targetType.eq(SWIMMING_CLASS))
                        .and(ticketEntity.isDeleted.isFalse())
                )
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
                    classTypeAndSubTypeIn(condition.classTypes(), condition.classSubTypes())
                )
                .fetchOne())
        .orElse(0L);

    val paged = new PageImpl<>(content, condition.pageable(), count);
    return new FindSwimmingPoolDetailClassesResponse(paged);
  }

  // 1순위 : 강습형태 순서 (단체강습>레슨>키즈>특별반>아쿠아로빅)
  private NumberTemplate<Integer> typeOrder() {
    return Expressions.numberTemplate(Integer.class,
        "CASE " +
            "WHEN {0} = '" + GROUP.name() + "' THEN 1 " +
            "WHEN {0} = '" + PRIVATE_LESSON.name() + "' THEN 2 " +
            "WHEN {0} = '" + KIDS_SWIMMING.name() + "' THEN 3 " +
            "WHEN {0} = '" + SPECIAL_CLASS.name() + "' THEN 4 " +
            "WHEN {0} = '" + AQUA_AEROBICS.name() + "' THEN 5 " +
            "ELSE 99 END",
        swimmingClassTypeEntity.name);
  }

  // 3순위 : 강습구분 선수 [ 단체강습(기초>초급>초중급>중급>중상급>상급>연수>마스터즈>종합), 나머지 강습구분은 오름차순 ]
  private NumberTemplate<Integer> subTypeOrder() {
    return Expressions.numberTemplate(Integer.class,
        "CASE WHEN {0} = '" + GROUP.name() + "' THEN " +
            "(CASE " +
            "WHEN {1} = '기초' THEN 1 " +
            "WHEN {1} = '초급' THEN 2 " +
            "WHEN {1} = '초중급' THEN 3 " +
            "WHEN {1} = '중급' THEN 4 " +
            "WHEN {1} = '중상급' THEN 5 " +
            "WHEN {1} = '상급' THEN 6 " +
            "WHEN {1} = '연수' THEN 7 " +
            "WHEN {1} = '마스터즈' THEN 8 " +
            "WHEN {1} = '종합' THEN 9 " +
            "ELSE 999 END) " +
            "ELSE 999 END",
        swimmingClassTypeEntity.name,
        swimmingClassSubTypeEntity.name
    );
  }

  // 4순위 : 요일 빠른 순서 (월>화>수>목>금>토>일)
  private NumberTemplate<Integer> minDayOfWeekOrder() {
    return Expressions.numberTemplate(Integer.class,
        "CASE " +
            "WHEN bitand({0}, {1}) > 0 THEN 1 " +  // 월 (bit 6 = 64)
            "WHEN bitand({0}, {2}) > 0 THEN 2 " +  // 화 (bit 5 = 32)
            "WHEN bitand({0}, {3}) > 0 THEN 3 " +  // 수 (bit 4 = 16)
            "WHEN bitand({0}, {4}) > 0 THEN 4 " +  // 목 (bit 3 = 8)
            "WHEN bitand({0}, {5}) > 0 THEN 5 " +  // 금 (bit 2 = 4)
            "WHEN bitand({0}, {6}) > 0 THEN 6 " +  // 토 (bit 1 = 2)
            "WHEN bitand({0}, {7}) > 0 THEN 7 " +  // 일 (bit 0 = 1)
            "ELSE 99 END",
        swimmingClassEntity.daysOfWeek,
        Expressions.constant(64),
        Expressions.constant(32),
        Expressions.constant(16),
        Expressions.constant(8),
        Expressions.constant(4),
        Expressions.constant(2),
        Expressions.constant(1)
    );
  }

  // 5순위 : 요일 개수가 적은 순서
  private NumberTemplate<Integer> daysOfWeekCountOrder() {
    return Expressions.numberTemplate(Integer.class,
        "(" +
            "(CASE WHEN bitand({0}, 1) > 0 THEN 1 ELSE 0 END) + " +
            "(CASE WHEN bitand({0}, 2) > 0 THEN 1 ELSE 0 END) + " +
            "(CASE WHEN bitand({0}, 4) > 0 THEN 1 ELSE 0 END) + " +
            "(CASE WHEN bitand({0}, 8) > 0 THEN 1 ELSE 0 END) + " +
            "(CASE WHEN bitand({0}, 16) > 0 THEN 1 ELSE 0 END) + " +
            "(CASE WHEN bitand({0}, 32) > 0 THEN 1 ELSE 0 END) + " +
            "(CASE WHEN bitand({0}, 64) > 0 THEN 1 ELSE 0 END)" +
            ")",
        swimmingClassEntity.daysOfWeek
    );
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
