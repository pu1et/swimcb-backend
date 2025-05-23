package com.project.swimcb.bo.reservation.adapter.out;

import static com.project.swimcb.bo.swimmingclass.domain.QSwimmingClass.swimmingClass;
import static com.project.swimcb.bo.swimmingclass.domain.QSwimmingClassTicket.swimmingClassTicket;
import static com.project.swimcb.swimmingpool.domain.QReservation.reservation;

import com.project.swimcb.bo.reservation.application.port.out.BoCancelReservationDsGateway;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.NoSuchElementException;
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
  public Long findSwimmingClassByReservationId(@NonNull Long reservationId) {

    val result = queryFactory.select(swimmingClass.id)
        .from(reservation)
        .join(swimmingClassTicket).on(reservation.ticketId.eq(swimmingClassTicket.id))
        .join(swimmingClass).on(swimmingClassTicket.swimmingClass.eq(swimmingClass))
        .where(
            reservation.id.eq(reservationId)
        )
        .fetchFirst();

    if (result == null) {
      throw new NoSuchElementException("클래스가 존재하지 않습니다 : " + reservationId);
    }

    return result;
  }

  @Override
  public void updateSwimmingClassReservedCount(@NonNull Long swimmingClassId, int count) {
    val result = queryFactory.update(swimmingClass)
        .set(swimmingClass.reservedCount, swimmingClass.reservedCount.add(count))
        .where(
            swimmingClass.id.eq(swimmingClassId)
        )
        .execute();

    if (result != 1) {
      throw new NoSuchElementException("클래스가 존재하지 않습니다 : " + swimmingClassId);
    }
  }

}
