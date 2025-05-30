package com.project.swimcb.bo.reservation.adapter.in;

import static com.project.swimcb.bo.swimmingclass.domain.QSwimmingClass.swimmingClass;
import static com.project.swimcb.bo.swimmingclass.domain.QSwimmingClassTicket.swimmingClassTicket;
import static com.project.swimcb.bo.swimmingpool.domain.QSwimmingPool.swimmingPool;
import static com.project.swimcb.swimmingpool.domain.QReservation.reservation;
import static com.project.swimcb.swimmingpool.domain.enums.CancellationReason.PAYMENT_DEADLINE_EXPIRED;
import static com.project.swimcb.swimmingpool.domain.enums.ReservationStatus.PAYMENT_PENDING;
import static com.project.swimcb.swimmingpool.domain.enums.ReservationStatus.RESERVATION_CANCELLED;
import static com.project.swimcb.swimmingpool.domain.enums.ReservationStatus.RESERVATION_PENDING;
import static com.querydsl.core.types.Projections.constructor;

import com.project.swimcb.bo.reservation.application.PaymentExpiredReservation;
import com.project.swimcb.bo.reservation.application.port.out.BoAutoCancelReservationsDsGateway;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoAutoCancelReservationsDataMapper implements BoAutoCancelReservationsDsGateway {

  private final JPAQueryFactory queryFactory;

  @Override
  public List<PaymentExpiredReservation> findPaymentExpiredReservationsBySwimmingPoolId(
      @NonNull Long swimmingPoolId) {

    val twentyFourHoursAgo = LocalDateTime.now().minusHours(24);

    return queryFactory.select(constructor(PaymentExpiredReservation.class,
            reservation.id,
            swimmingClass.id
        ))
        .from(reservation)
        .join(swimmingClass).on(reservation.swimmingClass.eq(swimmingClass))
        .join(swimmingPool).on(swimmingClass.swimmingPool.eq(swimmingPool))
        .where(
            swimmingPool.id.eq(swimmingPoolId),
            reservation.reservationStatus.eq(PAYMENT_PENDING),
            reservation.paymentPendingAt.lt(twentyFourHoursAgo)
        )
        .fetch();
  }

  @Override
  public List<PaymentExpiredReservation> findPaymentExpiredReservationsByMemberId(
      @NonNull Long memberId) {

    val twentyFourHoursAgo = LocalDateTime.now().minusHours(24);

    return queryFactory.select(constructor(PaymentExpiredReservation.class,
            reservation.id,
            reservation.swimmingClass.id
        ))
        .from(reservation)
        .where(
            reservation.member.id.eq(memberId),
            reservation.reservationStatus.eq(PAYMENT_PENDING),
            reservation.paymentPendingAt.lt(twentyFourHoursAgo)
        )
        .fetch();
  }

  @Override
  public void cancelExpiredReservations(@NonNull List<Long> reservationIds) {

    val now = LocalDateTime.now();

    queryFactory.update(reservation)
        .set(reservation.reservationStatus, RESERVATION_CANCELLED)
        .set(reservation.cancellationReason, PAYMENT_DEADLINE_EXPIRED)
        .set(reservation.canceledAt, now)
        .set(reservation.updatedAt, now)
        .where(
            reservation.id.in(reservationIds)
        )
        .execute();
  }

  @Override
  public void reduceSwimmingClassReservedCount(@NonNull List<Long> swimmingClassIds) {

    queryFactory.update(swimmingClass)
        .set(swimmingClass.reservedCount, swimmingClass.reservedCount.subtract(1))
        .set(swimmingClass.updatedAt, LocalDateTime.now())
        .where(
            swimmingClass.id.in(swimmingClassIds)
        )
        .execute();
  }

  @Override
  public List<Long> findReservationPendingReservations(
      @NonNull Map<Long, Long> reservationsCountByClass) {

    return reservationsCountByClass.entrySet().stream()
        .flatMap(entry -> {
          val swimmingClassId = entry.getKey();
          val count = entry.getValue();

          return queryFactory.select(reservation.id)
              .from(reservation)
              .join(swimmingClassTicket).on(reservation.ticketId.eq(swimmingClassTicket.id))
              .join(swimmingClass).on(swimmingClassTicket.swimmingClass.eq(swimmingClass))
              .where(
                  swimmingClass.id.eq(swimmingClassId),
                  reservation.reservationStatus.eq(RESERVATION_PENDING)
              )
              .orderBy(reservation.reservedAt.asc())
              .limit(count)
              .fetch()
              .stream();
        })
        .toList();
  }

  @Override
  public void convertPendingReservationsToPaymentPending(
      @NonNull List<Long> pendingReservationIds) {

    val now = LocalDateTime.now();

    queryFactory.update(reservation)
        .set(reservation.reservationStatus, PAYMENT_PENDING)
        .set(reservation.paymentPendingAt, now)
        .set(reservation.updatedAt, now)
        .where(
            reservation.id.in(pendingReservationIds)
        )
        .execute();
  }

}
