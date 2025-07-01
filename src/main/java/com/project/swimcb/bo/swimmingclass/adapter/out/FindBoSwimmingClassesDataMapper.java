package com.project.swimcb.bo.swimmingclass.adapter.out;

import static com.project.swimcb.db.entity.QReservationEntity.reservationEntity;
import static com.project.swimcb.db.entity.QSwimmingClassEntity.swimmingClassEntity;
import static com.project.swimcb.db.entity.QSwimmingClassSubTypeEntity.swimmingClassSubTypeEntity;
import static com.project.swimcb.db.entity.QSwimmingClassTypeEntity.swimmingClassTypeEntity;
import static com.project.swimcb.db.entity.QSwimmingInstructorEntity.swimmingInstructorEntity;
import static com.project.swimcb.db.entity.QTicketEntity.ticketEntity;
import static com.project.swimcb.swimmingpool.domain.enums.ReservationStatus.PAYMENT_COMPLETED;
import static com.project.swimcb.swimmingpool.domain.enums.TicketType.SWIMMING_CLASS;
import static com.querydsl.core.types.Projections.constructor;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

import com.project.swimcb.bo.swimmingclass.adapter.in.FindBoSwimmingClassesResponse;
import com.project.swimcb.bo.swimmingclass.adapter.in.FindBoSwimmingClassesResponse.Instructor;
import com.project.swimcb.bo.swimmingclass.adapter.in.FindBoSwimmingClassesResponse.RegistrationCapacity;
import com.project.swimcb.bo.swimmingclass.adapter.in.FindBoSwimmingClassesResponse.SwimmingClass;
import com.project.swimcb.bo.swimmingclass.adapter.in.FindBoSwimmingClassesResponse.Ticket;
import com.project.swimcb.bo.swimmingclass.adapter.in.FindBoSwimmingClassesResponse.TicketPriceRange;
import com.project.swimcb.bo.swimmingclass.adapter.in.FindBoSwimmingClassesResponse.Time;
import com.project.swimcb.bo.swimmingclass.adapter.in.FindBoSwimmingClassesResponse.Type;
import com.project.swimcb.bo.swimmingclass.application.out.FindBoSwimmingClassesDsGateway;
import com.project.swimcb.db.entity.TicketTargetType;
import com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.Builder;
import lombok.NonNull;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
class FindBoSwimmingClassesDataMapper implements FindBoSwimmingClassesDsGateway {

  private final JPAQueryFactory queryFactory;

  public FindBoSwimmingClassesDataMapper(EntityManager entityManager) {
    this.queryFactory = new JPAQueryFactory(entityManager);
  }

  @Override
  public FindBoSwimmingClassesResponse findBySwimmingPoolId(long swimmingPoolId, int month) {
    val groupingBySwimmingClass = findSwimmingClasses(swimmingPoolId, month)
        .stream().collect(groupingBy(i -> i.swimmingClassId));

    val completedReservationCountBySwimmingPool = findCompletedReservationCountBySwimmingPool(
        groupingBySwimmingClass.keySet())
        .stream()
        .collect(Collectors.toMap(
            BoCompletedReservationCount::swimmingClassId,
            BoCompletedReservationCount::completedReservationCount
        ));

    val result = groupingBySwimmingClass
        .entrySet()
        .stream()
        .map(i -> {
          val key = i.getKey();
          val value = i.getValue().getFirst();
          val days = days(value.daysOfWeek());
          val ticketMap = i.getValue().stream()
              .collect(toMap(BoSwimmingClass::ticketId, j -> j));
          val minimumTicketPrice = ticketMap.values().stream()
              .mapToInt(BoSwimmingClass::ticketPrice).min().orElse(0);
          val maximumTicketPrice = ticketMap.values().stream()
              .mapToInt(BoSwimmingClass::ticketPrice).max().orElse(0);
          val completedReservationCount = completedReservationCountBySwimmingPool.getOrDefault(
              i.getKey(), 0L).intValue();
          val remainingReservationCount = value.reservationLimitCount() - completedReservationCount;

          return SwimmingClass.builder()
              .swimmingClassId(key)
              .type(Type.builder()
                  .typeId(value.swimmingClassTypeId())
                  .typeName(value.classTypeName().getDescription())
                  .subTypeId(value.classSubTypeId())
                  .subTypeName(value.classSubTypeName())
                  .build())
              .days(days)
              .time(Time.builder()
                  .startTime(value.startTime())
                  .endTime(value.endTime())
                  .build())
              .instructor(instructor(value.instructorId(), value.instructorName()))
              .ticketPriceRange(TicketPriceRange.builder()
                  .minimumPrice(minimumTicketPrice)
                  .maximumPrice(maximumTicketPrice)
                  .build())
              .tickets(ticketMap.entrySet()
                  .stream()
                  .map(j -> Ticket.builder()
                      .id(j.getKey())
                      .name(j.getValue().ticketName())
                      .price(j.getValue().ticketPrice())
                      .build()
                  )
                  .toList())
              .registrationCapacity(RegistrationCapacity.builder()
                  .totalCapacity(value.totalCapacity())
                  .reservationLimitCount(value.reservationLimitCount())
                  .completedReservationCount(completedReservationCount)
                  .remainingReservationCount(remainingReservationCount)
                  .build())
              .isExposed(value.isExposed())
              .build();
        }).toList();

    return new FindBoSwimmingClassesResponse(result);
  }

  List<BoSwimmingClass> findSwimmingClasses(long swimmingPoolId, int month) {
    return queryFactory.select(
            constructor(BoSwimmingClass.class,
                swimmingClassEntity.id,
                swimmingClassTypeEntity.id,
                swimmingClassTypeEntity.name,
                swimmingClassSubTypeEntity.id,
                swimmingClassSubTypeEntity.name,
                swimmingClassEntity.daysOfWeek,
                swimmingClassEntity.startTime,
                swimmingClassEntity.endTime,
                swimmingInstructorEntity.id,
                swimmingInstructorEntity.name,
                ticketEntity.id,
                ticketEntity.name,
                ticketEntity.price,
                swimmingClassEntity.totalCapacity,
                swimmingClassEntity.reservationLimitCount,
                swimmingClassEntity.isVisible
            ))
        .from(swimmingClassEntity)
        .join(swimmingClassEntity.type, swimmingClassTypeEntity)
        .join(swimmingClassEntity.subType, swimmingClassSubTypeEntity)
        .leftJoin(swimmingClassEntity.instructor, swimmingInstructorEntity)
        .join(ticketEntity)
        .on(ticketEntity.targetId.eq(swimmingClassEntity.id))
        .where(
            swimmingClassEntity.swimmingPool.id.eq(swimmingPoolId),
            swimmingClassEntity.month.eq(month),
            ticketEntity.targetType.eq(TicketTargetType.SWIMMING_CLASS),
            ticketEntity.isDeleted.isFalse(),
            swimmingClassEntity.isCanceled.isFalse()
        )
        .orderBy(swimmingClassEntity.createdAt.asc())
        .fetch();
  }

  List<BoCompletedReservationCount> findCompletedReservationCountBySwimmingPool(
      @NonNull Set<Long> swimmingClassIds) {

    return queryFactory.select(
            constructor(BoCompletedReservationCount.class,
                swimmingClassEntity.id,
                reservationEntity.id.count()
            ))
        .from(reservationEntity)
        .join(ticketEntity)
        .on(
            reservationEntity.ticketType.eq(SWIMMING_CLASS),
            reservationEntity.ticketId.eq(ticketEntity.id)
        )
        .join(ticketEntity)
        .on(ticketEntity.targetId.eq(swimmingClassEntity.id))
        .where(
            swimmingClassEntity.id.in(swimmingClassIds),
            ticketEntity.targetType.eq(TicketTargetType.SWIMMING_CLASS),
            reservationEntity.reservationStatus.in(PAYMENT_COMPLETED)
        )
        .groupBy(swimmingClassEntity.id)
        .fetch();
  }

  private List<DayOfWeek> days(int days) {
    return IntStream.range(0, 7)
        .filter(i -> (days & (1 << (6 - i))) != 0)
        .mapToObj(i -> DayOfWeek.of(i + 1))
        .toList();
  }

  private Instructor instructor(Long instructorId, String instructorName) {
    if (instructorId == null) {
      return null;
    }
    return Instructor.builder()
        .id(instructorId)
        .name(instructorName)
        .build();
  }

  @Builder
  public record BoSwimmingClass(
      long swimmingClassId,
      long swimmingClassTypeId,
      SwimmingClassTypeName classTypeName,
      long classSubTypeId,
      String classSubTypeName,
      int daysOfWeek,
      LocalTime startTime,
      LocalTime endTime,
      Long instructorId,
      String instructorName,
      long ticketId,
      String ticketName,
      int ticketPrice,
      int totalCapacity,
      int reservationLimitCount,
      boolean isExposed
  ) {

  }

  @Builder
  public record BoCompletedReservationCount(
      long swimmingClassId,
      long completedReservationCount
  ) {

  }

}
