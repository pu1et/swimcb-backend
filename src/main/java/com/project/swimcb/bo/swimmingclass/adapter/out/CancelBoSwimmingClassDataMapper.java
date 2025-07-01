package com.project.swimcb.bo.swimmingclass.adapter.out;

import static com.project.swimcb.db.entity.QReservationEntity.reservationEntity;
import static com.project.swimcb.db.entity.QTicketEntity.ticketEntity;
import static com.project.swimcb.db.entity.TicketTargetType.SWIMMING_CLASS;
import static com.project.swimcb.swimmingpool.domain.enums.ReservationStatus.RESERVATION_CANCELLED;

import com.project.swimcb.bo.swimmingclass.application.out.CancelBoSwimmingClassDsGateway;
import com.project.swimcb.db.repository.SwimmingClassSubTypeRepository;
import com.project.swimcb.db.repository.SwimmingClassTypeRepository;
import com.project.swimcb.db.repository.SwimmingInstructorRepository;
import com.project.swimcb.swimmingpool.domain.enums.CancellationReason;
import com.project.swimcb.swimmingpool.domain.enums.ReservationStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class CancelBoSwimmingClassDataMapper implements CancelBoSwimmingClassDsGateway {

  private final JPAQueryFactory queryFactory;
  private final SwimmingClassTypeRepository swimmingClassTypeRepository;
  private final SwimmingClassSubTypeRepository swimmingClassSubTypeRepository;
  private final SwimmingInstructorRepository swimmingInstructorRepository;
  private final EntityManager entityManager;

  @Override
  public void cancelAllReservationsBySwimmingClassIdAndReservationStatusIn(
      @NonNull Long swimmingClassId, @NonNull List<ReservationStatus> reservationStatuses,
      @NonNull CancellationReason cancellationReason) {

    val reservationIds = queryFactory.select(reservationEntity.id)
        .from(reservationEntity)
        .join(ticketEntity)
        .on(reservationEntity.ticketId.eq(ticketEntity.id))
        .where(
            ticketEntity.targetId.eq(swimmingClassId),
            ticketEntity.targetType.eq(SWIMMING_CLASS),
            reservationEntity.reservationStatus.in(reservationStatuses)
        )
        .fetch();

    queryFactory.update(reservationEntity)
        .set(reservationEntity.reservationStatus, RESERVATION_CANCELLED)
        .set(reservationEntity.cancellationReason, cancellationReason)
        .set(reservationEntity.canceledAt, LocalDateTime.now())
        .set(reservationEntity.updatedAt, LocalDateTime.now())
        .where(
            reservationEntity.id.in(reservationIds)
        )
        .execute();
  }

  @Override
  public boolean existsReservationBySwimmingClassIdReservationStatusIn(
      @NonNull Long swimmingClassId, @NonNull List<ReservationStatus> reservationStatuses) {

    val count = queryFactory.select(reservationEntity.count())
        .from(reservationEntity)
        .join(ticketEntity)
        .on(reservationEntity.ticketId.eq(ticketEntity.id))
        .where(
            ticketEntity.targetId.eq(swimmingClassId),
            ticketEntity.targetType.eq(SWIMMING_CLASS),
            reservationEntity.reservationStatus.in(reservationStatuses)
        )
        .fetchOne();
    return count != null && count > 0;
  }

}
