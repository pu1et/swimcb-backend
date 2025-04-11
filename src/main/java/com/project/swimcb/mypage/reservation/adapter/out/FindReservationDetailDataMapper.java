package com.project.swimcb.mypage.reservation.adapter.out;

import static com.project.swimcb.bo.swimmingclass.domain.QSwimmingClass.swimmingClass;
import static com.project.swimcb.bo.swimmingclass.domain.QSwimmingClassSubType.swimmingClassSubType;
import static com.project.swimcb.bo.swimmingclass.domain.QSwimmingClassTicket.swimmingClassTicket;
import static com.project.swimcb.bo.swimmingclass.domain.QSwimmingClassType.swimmingClassType;
import static com.project.swimcb.bo.swimmingpool.domain.QSwimmingPool.swimmingPool;
import static com.project.swimcb.bo.swimmingpool.domain.QSwimmingPoolImage.swimmingPoolImage;
import static com.project.swimcb.swimmingpool.domain.QReservation.reservation;
import static com.querydsl.core.types.Projections.constructor;

import com.project.swimcb.mypage.reservation.application.port.out.FindReservationDetailGateway;
import com.project.swimcb.mypage.reservation.domain.ReservationDetail;
import com.project.swimcb.mypage.reservation.domain.ReservationDetail.Payment;
import com.project.swimcb.mypage.reservation.domain.ReservationDetail.Reservation;
import com.project.swimcb.mypage.reservation.domain.ReservationDetail.SwimmingClass;
import com.project.swimcb.mypage.reservation.domain.ReservationDetail.SwimmingPool;
import com.project.swimcb.mypage.reservation.domain.ReservationDetail.Ticket;
import com.project.swimcb.swimmingpool.domain.enums.PaymentMethod;
import com.project.swimcb.swimmingpool.domain.enums.ReservationStatus;
import com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName;
import com.querydsl.core.annotations.QueryProjection;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindReservationDetailDataMapper implements FindReservationDetailGateway {

  private final JPAQueryFactory queryFactory;

  @Override
  public ReservationDetail findReservationDetail(long reservationId) {
    val result = queryFactory.select(constructor(QueryReservationDetail.class,
            swimmingPool.id,
            swimmingPool.name,
            swimmingPoolImage.path,
            swimmingClass.id,
            swimmingClassType.name,
            swimmingClassSubType.name,
            swimmingClassTicket.id,
            swimmingClassTicket.name,
            swimmingClassTicket.price,
            reservation.reservationStatus,
            reservation.reservedAt,
            reservation.paymentMethod,
            reservation.paymentAmount
        ))
        .from(reservation)
        .join(swimmingClassTicket).on(reservation.ticketId.eq(swimmingClassTicket.id))
        .join(swimmingClass).on(swimmingClassTicket.swimmingClass.eq(swimmingClass))
        .join(swimmingClassType).on(swimmingClass.type.eq(swimmingClassType))
        .join(swimmingClassSubType).on(swimmingClass.subType.eq(swimmingClassSubType))
        .join(swimmingPool).on(swimmingClass.swimmingPool.eq(swimmingPool))
        .join(swimmingPoolImage).on(swimmingPool.eq(swimmingPoolImage.swimmingPool))
        .where(
            reservation.id.eq(reservationId)
        )
        .fetchFirst();

    if (result == null) {
      throw new NoSuchElementException("예약 상세 정보가 존재하지 않습니다 : " + reservationId);
    }

    return ReservationDetail.builder()
        .swimmingPool(
            SwimmingPool.builder()
                .id(result.swimmingPoolId())
                .name(result.swimmingPoolName())
                .imagePath(result.swimmingPoolImagePath())
                .build()
        )
        .swimmingClass(
            SwimmingClass.builder()
                .id(result.swimmingClassId())
                .type(result.swimmingClassType())
                .subType(result.swimmingClassSubType())
                .build()
        )
        .ticket(
            Ticket.builder()
                .id(result.ticketId())
                .name(result.ticketName())
                .price(result.ticketPrice())
                .build()
        )
        .reservation(
            Reservation.builder()
                .id(reservationId)
                .status(result.reservationStatus())
                .reservedAt(result.reservedAt())
                .build()
        )
        .payment(
            Payment.builder()
                .method(result.paymentMethod())
                .amount(result.paymentAmount())
                .build()
        )
        .build();
  }

  @Builder
  public record QueryReservationDetail(
      long swimmingPoolId,
      String swimmingPoolName,
      String swimmingPoolImagePath,
      long swimmingClassId,
      SwimmingClassTypeName swimmingClassType,
      String swimmingClassSubType,
      long ticketId,
      String ticketName,
      int ticketPrice,
      ReservationStatus reservationStatus,
      LocalDateTime reservedAt,
      PaymentMethod paymentMethod,
      int paymentAmount
  ) {

    @QueryProjection
    public QueryReservationDetail {
    }
  }
}
