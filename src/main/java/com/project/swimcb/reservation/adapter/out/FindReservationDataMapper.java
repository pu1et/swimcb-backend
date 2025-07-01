package com.project.swimcb.reservation.adapter.out;

import static com.project.swimcb.db.entity.QReservationEntity.reservationEntity;
import static com.project.swimcb.db.entity.QSwimmingClassEntity.swimmingClassEntity;
import static com.project.swimcb.db.entity.QSwimmingClassSubTypeEntity.swimmingClassSubTypeEntity;
import static com.project.swimcb.db.entity.QSwimmingClassTypeEntity.swimmingClassTypeEntity;
import static com.project.swimcb.db.entity.QSwimmingPoolEntity.swimmingPoolEntity;
import static com.project.swimcb.db.entity.QTicketEntity.ticketEntity;
import static com.project.swimcb.db.entity.TicketTargetType.SWIMMING_CLASS;
import static com.querydsl.core.types.Projections.constructor;

import com.project.swimcb.db.entity.AccountNo;
import com.project.swimcb.reservation.application.port.out.FindReservationGateway;
import com.project.swimcb.reservation.domain.ReservationInfo;
import com.project.swimcb.swimmingpool.domain.enums.PaymentMethod;
import com.project.swimcb.swimmingpool.domain.enums.ReservationStatus;
import com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName;
import com.querydsl.core.annotations.QueryProjection;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.Builder;
import lombok.NonNull;
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
            swimmingPoolEntity.id,
            swimmingPoolEntity.name,
            swimmingPoolEntity.accountNo,

            swimmingClassEntity.id,
            swimmingClassEntity.month,
            swimmingClassTypeEntity.name,
            swimmingClassSubTypeEntity.name,
            swimmingClassEntity.daysOfWeek,
            swimmingClassEntity.startTime,
            swimmingClassEntity.endTime,
            swimmingClassEntity.reservationLimitCount,
            swimmingClassEntity.reservedCount,

            ticketEntity.id,
            ticketEntity.name,
            ticketEntity.price,

            reservationEntity.reservedAt,
            reservationEntity.reservationStatus,
            reservationEntity.paymentMethod
        ))
        .from(reservationEntity)
        .join(ticketEntity)
        .on(reservationEntity.ticketId.eq(ticketEntity.id))
        .join(swimmingClassEntity)
        .on(ticketEntity.targetId.eq(swimmingClassEntity.id))
        .join(swimmingClassTypeEntity).on(swimmingClassEntity.type.eq(swimmingClassTypeEntity))
        .join(swimmingClassSubTypeEntity)
        .on(swimmingClassEntity.subType.eq(swimmingClassSubTypeEntity))
        .join(swimmingPoolEntity).on(swimmingClassEntity.swimmingPool.eq(swimmingPoolEntity))
        .where(
            reservationEntity.id.eq(reservationId),
            ticketEntity.targetType.eq(SWIMMING_CLASS)
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
                .accountNo(result.accountNo())
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
        .reservation(
            ReservationInfo.Reservation.builder()
                .id(reservationId)
                .reservedAt(result.reservedAt())
                .waitingNo(waitingNo(result))
                .build()
        )
        .payment(
            ReservationInfo.Payment.builder()
                .method(result.paymentMethod())
                .build()
        )
        .build();
  }

  private Integer waitingNo(@NonNull QueryReservationInfo result) {
    if (result.reservationStatus() != ReservationStatus.RESERVATION_PENDING) {
      return null;
    }

    val previousPendingReservationCount = Optional.ofNullable(
            queryFactory.select(reservationEntity.id.count())
                .from(reservationEntity)
                .where(
                    reservationEntity.swimmingClass.id.eq(result.swimmingClassId),
                    reservationEntity.reservationStatus.eq(ReservationStatus.RESERVATION_PENDING),
                    reservationEntity.reservedAt.lt(result.reservedAt) // 예약 시간 이전의 예약들만 고려
                )
                .fetchOne())
        .orElse(0L)
        .intValue();
    return previousPendingReservationCount + 1;
  }

  @Builder
  public record QueryReservationInfo(
      long swimmingPoolId,
      String swimmingPoolName,
      AccountNo accountNo,

      long swimmingClassId,
      int month,
      SwimmingClassTypeName swimmingClassType,
      String swimmingClassSubType,
      int daysOfWeek,
      LocalTime startTime,
      LocalTime endTime,
      int reservationLimitCount,
      int reservedCount,

      long ticketId,
      String ticketName,
      int ticketPrice,

      LocalDateTime reservedAt,
      ReservationStatus reservationStatus,
      PaymentMethod paymentMethod
  ) {

    @QueryProjection
    public QueryReservationInfo {
    }

  }

}
