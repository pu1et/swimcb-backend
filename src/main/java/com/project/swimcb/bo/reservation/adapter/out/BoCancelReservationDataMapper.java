package com.project.swimcb.bo.reservation.adapter.out;

import static com.project.swimcb.db.entity.QReservationEntity.reservationEntity;
import static com.project.swimcb.db.entity.QSwimmingClassEntity.swimmingClassEntity;
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
    val result = queryFactory.update(swimmingClassEntity)
        .set(swimmingClassEntity.reservedCount, swimmingClassEntity.reservedCount.add(count))
        .set(swimmingClassEntity.updatedAt, LocalDateTime.now())
        .where(
            swimmingClassEntity.id.eq(swimmingClassId)
        )
        .execute();

    if (result != 1) {
      throw new NoSuchElementException("클래스가 존재하지 않습니다 : " + swimmingClassId);
    }
  }

  @Override
  public Optional<Long> findFirstWaitingReservationIdBySwimmingClassId(
      @NonNull Long swimmingClassId) {

    return Optional.ofNullable(queryFactory.select(reservationEntity.id)
        .from(reservationEntity)
        .where(
            reservationEntity.swimmingClass.id.eq(swimmingClassId),
            reservationEntity.reservationStatus.eq(RESERVATION_PENDING)
        )
        .orderBy(reservationEntity.reservedAt.asc())
        .fetchFirst());
  }

  @Override
  public List<Long> findWaitingReservationIdsBySwimmingClassIdLimit(@NonNull Long swimmingClassId,
      @NonNull Integer limit) {

    return queryFactory.select(reservationEntity.id)
        .from(reservationEntity)
        .where(
            reservationEntity.swimmingClass.id.eq(swimmingClassId),
            reservationEntity.reservationStatus.eq(RESERVATION_PENDING)
        )
        .orderBy(reservationEntity.reservedAt.asc())
        .limit(limit)
        .fetch();
  }

  @Override
  public void updateReservationStatusToPaymentPending(@NonNull Long reservationId) {
    val now = LocalDateTime.now();
    queryFactory.update(reservationEntity)
        .set(reservationEntity.reservationStatus, PAYMENT_PENDING)
        .set(reservationEntity.paymentPendingAt, now)
        .set(reservationEntity.updatedAt, now)
        .where(
            reservationEntity.id.eq(reservationId)
        )
        .execute();
  }

  @Override
  public void updateReservationStatusToPaymentPending(@NonNull List<Long> reservationIds) {
    val now = LocalDateTime.now();
    queryFactory.update(reservationEntity)
        .set(reservationEntity.reservationStatus, PAYMENT_PENDING)
        .set(reservationEntity.paymentPendingAt, now)
        .set(reservationEntity.updatedAt, now)
        .where(
            reservationEntity.id.in(reservationIds)
        )
        .execute();
  }

}
