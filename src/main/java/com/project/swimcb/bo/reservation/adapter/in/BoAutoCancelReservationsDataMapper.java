package com.project.swimcb.bo.reservation.adapter.in;

import static com.project.swimcb.db.entity.QReservationEntity.reservationEntity;
import static com.project.swimcb.db.entity.QSwimmingClassEntity.swimmingClassEntity;
import static com.project.swimcb.db.entity.QSwimmingPoolEntity.swimmingPoolEntity;
import static com.project.swimcb.db.entity.QTicketEntity.ticketEntity;
import static com.project.swimcb.db.entity.TicketTargetType.SWIMMING_CLASS;
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
            reservationEntity.id,
            swimmingClassEntity.id
        ))
        .from(reservationEntity)
        .join(swimmingClassEntity).on(reservationEntity.swimmingClass.eq(swimmingClassEntity))
        .join(swimmingPoolEntity).on(swimmingClassEntity.swimmingPool.eq(swimmingPoolEntity))
        .where(
            swimmingPoolEntity.id.eq(swimmingPoolId),
            reservationEntity.reservationStatus.eq(PAYMENT_PENDING),
            reservationEntity.paymentPendingAt.lt(twentyFourHoursAgo)
        )
        .fetch();
  }

  @Override
  public List<PaymentExpiredReservation> findPaymentExpiredReservationsByMemberId(
      @NonNull Long memberId) {

    val twentyFourHoursAgo = LocalDateTime.now().minusHours(24);

    return queryFactory.select(constructor(PaymentExpiredReservation.class,
            reservationEntity.id,
            reservationEntity.swimmingClass.id
        ))
        .from(reservationEntity)
        .join(reservationEntity.swimmingClass, swimmingClassEntity)
        .where(
            reservationEntity.member.id.eq(memberId),
            reservationEntity.reservationStatus.eq(PAYMENT_PENDING),
            reservationEntity.paymentPendingAt.lt(twentyFourHoursAgo)
        )
        .fetch();
  }

  @Override
  public void cancelExpiredReservations(@NonNull List<Long> reservationIds) {

    val now = LocalDateTime.now();

    queryFactory.update(reservationEntity)
        .set(reservationEntity.reservationStatus, RESERVATION_CANCELLED)
        .set(reservationEntity.cancellationReason, PAYMENT_DEADLINE_EXPIRED)
        .set(reservationEntity.canceledAt, now)
        .set(reservationEntity.updatedAt, now)
        .where(
            reservationEntity.id.in(reservationIds)
        )
        .execute();
  }

  @Override
  public void reduceSwimmingClassReservedCount(@NonNull List<Long> swimmingClassIds) {

    queryFactory.update(swimmingClassEntity)
        .set(swimmingClassEntity.reservedCount, swimmingClassEntity.reservedCount.subtract(1))
        .set(swimmingClassEntity.updatedAt, LocalDateTime.now())
        .where(
            swimmingClassEntity.id.in(swimmingClassIds)
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

          return queryFactory.select(reservationEntity.id)
              .from(reservationEntity)
              .join(ticketEntity)
              .on(reservationEntity.ticketId.eq(ticketEntity.id))
              .join(swimmingClassEntity)
              .on(ticketEntity.targetId.eq(swimmingClassEntity.id))
              .where(
                  swimmingClassEntity.id.eq(swimmingClassId),
                  ticketEntity.targetType.eq(SWIMMING_CLASS),
                  reservationEntity.reservationStatus.eq(RESERVATION_PENDING)
              )
              .orderBy(reservationEntity.reservedAt.asc())
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

    queryFactory.update(reservationEntity)
        .set(reservationEntity.reservationStatus, PAYMENT_PENDING)
        .set(reservationEntity.paymentPendingAt, now)
        .set(reservationEntity.updatedAt, now)
        .where(
            reservationEntity.id.in(pendingReservationIds)
        )
        .execute();
  }

}
