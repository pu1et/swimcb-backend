package com.project.swimcb.bo.reservation.adapter.out;

import static com.project.swimcb.bo.swimmingclass.domain.QSwimmingClass.swimmingClass;
import static com.project.swimcb.swimmingpool.domain.QReservation.reservation;
import static com.project.swimcb.swimmingpool.domain.enums.ReservationStatus.PAYMENT_PENDING;
import static com.project.swimcb.swimmingpool.domain.enums.ReservationStatus.RESERVATION_PENDING;

import com.project.swimcb.bo.reservation.application.port.out.BoCancelReservationDsGateway;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.NonNull;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
class BoCancelReservationDataMapper implements BoCancelReservationDsGateway {

  private final JPAQueryFactory queryFactory;

  public BoCancelReservationDataMapper(EntityManager entityManager) {
    this.queryFactory = new JPAQueryFactory(entityManager);
  }

  @Override
  public void updateSwimmingClassReservedCount(@NonNull Long swimmingClassId, int count) {
    val result = queryFactory.update(swimmingClass)
        .set(swimmingClass.reservedCount, swimmingClass.reservedCount.add(count))
        .set(swimmingClass.updatedAt, LocalDateTime.now())
        .where(
            swimmingClass.id.eq(swimmingClassId)
        )
        .execute();

    if (result != 1) {
      throw new NoSuchElementException("클래스가 존재하지 않습니다 : " + swimmingClassId);
    }
  }

  @Override
  public Optional<Long> findFirstWaitingReservationIdBySwimmingClassId(
      @NonNull Long swimmingClassId) {

    return Optional.ofNullable(queryFactory.select(reservation.id)
        .from(reservation)
        .where(
            reservation.swimmingClass.id.eq(swimmingClassId),
            reservation.reservationStatus.eq(RESERVATION_PENDING)
        )
        .orderBy(reservation.reservedAt.asc())
        .fetchFirst());
  }

  @Override
  public List<Long> findWaitingReservationIdsBySwimmingClassIdLimit(@NonNull Long swimmingClassId,
      @NonNull Integer limit) {

    return queryFactory.select(reservation.id)
        .from(reservation)
        .where(
            reservation.swimmingClass.id.eq(swimmingClassId),
            reservation.reservationStatus.eq(RESERVATION_PENDING)
        )
        .orderBy(reservation.reservedAt.asc())
        .limit(limit)
        .fetch();
  }

  @Override
  public void updateReservationStatusToPaymentPending(@NonNull Long reservationId) {
    val now = LocalDateTime.now();
    queryFactory.update(reservation)
        .set(reservation.reservationStatus, PAYMENT_PENDING)
        .set(reservation.paymentPendingAt, now)
        .set(reservation.updatedAt, now)
        .where(
            reservation.id.eq(reservationId)
        )
        .execute();
  }

  @Override
  public void updateReservationStatusToPaymentPending(@NonNull List<Long> reservationIds) {
    val now = LocalDateTime.now();
    queryFactory.update(reservation)
        .set(reservation.reservationStatus, PAYMENT_PENDING)
        .set(reservation.paymentPendingAt, now)
        .set(reservation.updatedAt, now)
        .where(
            reservation.id.in(reservationIds)
        )
        .execute();
  }

}
