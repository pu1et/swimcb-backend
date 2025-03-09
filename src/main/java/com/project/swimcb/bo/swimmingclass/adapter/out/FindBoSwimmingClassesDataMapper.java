package com.project.swimcb.bo.swimmingclass.adapter.out;

import static com.project.swimcb.bo.instructor.domain.QSwimmingInstructor.swimmingInstructor;
import static com.project.swimcb.bo.swimmingclass.domain.QSwimmingClass.swimmingClass;
import static com.project.swimcb.bo.swimmingclass.domain.QSwimmingClassSubType.swimmingClassSubType;
import static com.project.swimcb.bo.swimmingclass.domain.QSwimmingClassTicket.swimmingClassTicket;
import static com.project.swimcb.bo.swimmingclass.domain.QSwimmingClassType.swimmingClassType;
import static com.querydsl.core.types.Projections.constructor;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

import com.project.swimcb.bo.swimmingclass.adapter.in.FindBoSwimmingClassesResponse;
import com.project.swimcb.bo.swimmingclass.adapter.in.FindBoSwimmingClassesResponse.Days;
import com.project.swimcb.bo.swimmingclass.adapter.in.FindBoSwimmingClassesResponse.Instructor;
import com.project.swimcb.bo.swimmingclass.adapter.in.FindBoSwimmingClassesResponse.RegistrationCapacity;
import com.project.swimcb.bo.swimmingclass.adapter.in.FindBoSwimmingClassesResponse.SwimmingClass;
import com.project.swimcb.bo.swimmingclass.adapter.in.FindBoSwimmingClassesResponse.Ticket;
import com.project.swimcb.bo.swimmingclass.adapter.in.FindBoSwimmingClassesResponse.TicketPriceRange;
import com.project.swimcb.bo.swimmingclass.adapter.in.FindBoSwimmingClassesResponse.Time;
import com.project.swimcb.bo.swimmingclass.adapter.in.FindBoSwimmingClassesResponse.Type;
import com.project.swimcb.bo.swimmingclass.application.out.FindBoSwimmingClassesDsGateway;
import com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.time.LocalTime;
import java.util.List;
import lombok.Builder;
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

    val result = groupingBySwimmingClass
        .entrySet()
        .stream()
        .map(i -> {
          val key = i.getKey();
          val value = i.getValue().getFirst();
          val ticketMap = i.getValue().stream()
              .collect(toMap(BoSwimmingClass::ticketId, j -> j));
          val minimumTicketPrice = ticketMap.values().stream()
              .mapToInt(BoSwimmingClass::ticketPrice).min().orElse(0);
          val maximumTicketPrice = ticketMap.values().stream()
              .mapToInt(BoSwimmingClass::ticketPrice).max().orElse(0);

          return SwimmingClass.builder()
              .swimmingClassId(key)
              .type(Type.builder()
                  .typeId(value.swimmingClassTypeId())
                  .typeName(value.classTypeName().getDescription())
                  .subTypeId(value.classSubTypeId())
                  .subTypeName(value.classSubTypeName())
                  .build())
              .days(Days.builder()
                  .isMonday(value.isMonday())
                  .isTuesday(value.isTuesday())
                  .isWednesday(value.isWednesday())
                  .isThursday(value.isThursday())
                  .isFriday(value.isFriday())
                  .isSaturday(value.isSaturday())
                  .isSunday(value.isSunday())
                  .build())
              .time(Time.builder()
                  .startTime(value.startTime())
                  .endTime(value.endTime())
                  .build())
              .instructor(Instructor.builder()
                  .id(value.instructorId())
                  .name(value.instructorName())
                  .build())
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
                  .build())
              .build();
        }).toList();

    return new FindBoSwimmingClassesResponse(result);
  }

  List<BoSwimmingClass> findSwimmingClasses(long swimmingPoolId, int month) {
    return queryFactory.select(
            constructor(BoSwimmingClass.class,
                swimmingClass.id,
                swimmingClassType.id,
                swimmingClassType.name,
                swimmingClassSubType.id,
                swimmingClassSubType.name,
                swimmingClass.isMonday,
                swimmingClass.isTuesday,
                swimmingClass.isWednesday,
                swimmingClass.isThursday,
                swimmingClass.isFriday,
                swimmingClass.isSaturday,
                swimmingClass.isSunday,
                swimmingClass.startTime,
                swimmingClass.endTime,
                swimmingInstructor.id,
                swimmingInstructor.name,
                swimmingClassTicket.id,
                swimmingClassTicket.name,
                swimmingClassTicket.price,
                swimmingClass.totalCapacity,
                swimmingClass.reservationLimitCount,
                swimmingClass.isVisible
            ))
        .from(swimmingClass)
        .join(swimmingClass.type, swimmingClassType)
        .join(swimmingClass.subType, swimmingClassSubType)
        .join(swimmingClass.instructor, swimmingInstructor)
        .join(swimmingClassTicket).on(swimmingClassTicket.swimmingClass.eq(swimmingClass))
        .where(
            swimmingClass.swimmingPool.id.eq(swimmingPoolId),
            swimmingClass.month.eq(month)
        )
        .orderBy(swimmingClass.createdAt.asc())
        .fetch();
  }

  @Builder
  public record BoSwimmingClass(
      long swimmingClassId,
      long swimmingClassTypeId,
      SwimmingClassTypeName classTypeName,
      long classSubTypeId,
      String classSubTypeName,
      boolean isMonday,
      boolean isTuesday,
      boolean isWednesday,
      boolean isThursday,
      boolean isFriday,
      boolean isSaturday,
      boolean isSunday,
      LocalTime startTime,
      LocalTime endTime,
      long instructorId,
      String instructorName,
      long ticketId,
      String ticketName,
      int ticketPrice,
      int totalCapacity,
      int reservationLimitCount,
      boolean isExposed
  ) {

  }
}
