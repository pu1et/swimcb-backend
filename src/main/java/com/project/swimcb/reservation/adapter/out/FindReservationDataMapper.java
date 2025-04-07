package com.project.swimcb.reservation.adapter.out;

import static com.project.swimcb.bo.swimmingclass.domain.QSwimmingClass.swimmingClass;
import static com.project.swimcb.bo.swimmingclass.domain.QSwimmingClassSubType.swimmingClassSubType;
import static com.project.swimcb.bo.swimmingclass.domain.QSwimmingClassTicket.swimmingClassTicket;
import static com.project.swimcb.bo.swimmingclass.domain.QSwimmingClassType.swimmingClassType;
import static com.project.swimcb.bo.swimmingpool.domain.QSwimmingPool.swimmingPool;
import static com.project.swimcb.swimmingpool.domain.QReservation.reservation;
import static com.querydsl.core.types.Projections.constructor;

import com.project.swimcb.reservation.application.port.out.FindReservationGateway;
import com.project.swimcb.reservation.domain.ReservationInfo;
import com.project.swimcb.swimmingpool.domain.enums.PaymentMethod;
import com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName;
import com.querydsl.core.annotations.QueryProjection;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalTime;
import java.util.NoSuchElementException;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindReservationDataMapper implements FindReservationGateway {

  private final JPAQueryFactory queryFactory;

  @Override
  public ReservationInfo findReservation(long reservationId) {
    val result = queryFactory.select(constructor(QueryReservationInfo.class,
            swimmingPool.id,
            swimmingPool.name,
            swimmingClass.id,
            swimmingClass.month,
            swimmingClassType.name,
            swimmingClassSubType.name,
            swimmingClass.daysOfWeek,
            swimmingClass.startTime,
            swimmingClass.endTime,
            swimmingClassTicket.id,
            swimmingClassTicket.name,
            swimmingClassTicket.price,
            reservation.paymentMethod
        ))
        .from(reservation)
        .join(swimmingClassTicket).on(reservation.ticketId.eq(swimmingClassTicket.id))
        .join(swimmingClass).on(swimmingClassTicket.swimmingClass.eq(swimmingClass))
        .join(swimmingClassType).on(swimmingClass.type.eq(swimmingClassType))
        .join(swimmingClassSubType).on(swimmingClass.subType.eq(swimmingClassSubType))
        .join(swimmingPool).on(swimmingClass.swimmingPool.eq(swimmingPool))
        .where(
            reservation.id.eq(reservationId)
        )
        .fetchFirst();

    if (result == null) {
      throw new NoSuchElementException("예약 정보가 존재하지 않습니다 : " + reservationId);
    }

    return ReservationInfo.builder()
        .swimmingPool(
            ReservationInfo.SwimmingPool.builder()
                .id(result.swimmingPoolId())
                .name(result.swimmingPoolName())
                .build()
        )
        .swimmingClass(
            ReservationInfo.SwimmingClass.builder()
                .id(result.swimmingClassId())
                .month(result.month())
                .type(result.swimmingClassType())
                .subType(result.swimmingClassSubType())
                .daysOfWeek(result.daysOfWeek())
                .startTime(result.startTime())
                .endTime(result.endTime())
                .build()
        )
        .ticket(
            ReservationInfo.Ticket.builder()
                .id(result.ticketId())
                .name(result.ticketName())
                .price(result.ticketPrice())
                .build()
        )
        .paymentMethod(result.paymentMethod())
        .build();
  }

  @Builder
  public record QueryReservationInfo(
      long swimmingPoolId,
      String swimmingPoolName,
      long swimmingClassId,
      int month,
      SwimmingClassTypeName swimmingClassType,
      String swimmingClassSubType,
      int daysOfWeek,
      LocalTime startTime,
      LocalTime endTime,
      long ticketId,
      String ticketName,
      int ticketPrice,
      PaymentMethod paymentMethod
  ) {

    @QueryProjection
    public QueryReservationInfo {
    }
  }
}
