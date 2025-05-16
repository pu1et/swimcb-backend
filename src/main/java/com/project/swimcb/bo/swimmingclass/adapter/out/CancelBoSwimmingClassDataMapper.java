package com.project.swimcb.bo.swimmingclass.adapter.out;

import static com.project.swimcb.bo.swimmingclass.domain.QSwimmingClass.swimmingClass;
import static com.project.swimcb.bo.swimmingclass.domain.QSwimmingClassTicket.swimmingClassTicket;
import static com.project.swimcb.swimmingpool.domain.QReservation.*;
import static com.project.swimcb.swimmingpool.domain.enums.ReservationStatus.*;

import com.project.swimcb.bo.instructor.domain.SwimmingInstructor;
import com.project.swimcb.bo.instructor.domain.SwimmingInstructorRepository;
import com.project.swimcb.bo.swimmingclass.application.out.CancelBoSwimmingClassDsGateway;
import com.project.swimcb.bo.swimmingclass.application.out.UpdateBoSwimmingClassDsGateway;
import com.project.swimcb.bo.swimmingclass.domain.SwimmingClassSubType;
import com.project.swimcb.bo.swimmingclass.domain.SwimmingClassSubTypeRepository;
import com.project.swimcb.bo.swimmingclass.domain.SwimmingClassType;
import com.project.swimcb.bo.swimmingclass.domain.SwimmingClassTypeRepository;
import com.project.swimcb.bo.swimmingclass.domain.UpdateBoSwimmingClassCommand;
import com.project.swimcb.swimmingpool.domain.QReservation;
import com.project.swimcb.swimmingpool.domain.enums.CancellationReason;
import com.project.swimcb.swimmingpool.domain.enums.ReservationStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
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

    val reservationIds = queryFactory.select(reservation.id)
        .from(reservation)
        .join(swimmingClassTicket).on(reservation.ticketId.eq(swimmingClassTicket.id))
        .where(
            swimmingClassTicket.swimmingClass.id.eq(swimmingClassId),
            reservation.reservationStatus.in(reservationStatuses)
        )
        .fetch();

    queryFactory.update(reservation)
        .set(reservation.reservationStatus, RESERVATION_CANCELLED)
        .set(reservation.cancellationReason, cancellationReason)
        .set(reservation.canceledAt, LocalDateTime.now())
        .set(reservation.updatedAt, LocalDateTime.now())
        .where(
            reservation.id.in(reservationIds)
        )
        .execute();
  }

  @Override
  public boolean existsReservationBySwimmingClassIdReservationStatusIn(
      @NonNull Long swimmingClassId, @NonNull List<ReservationStatus> reservationStatuses) {

     val count = queryFactory.select(reservation.count())
        .from(reservation)
        .join(swimmingClassTicket).on(reservation.ticketId.eq(swimmingClassTicket.id))
        .where(
            swimmingClassTicket.swimmingClass.id.eq(swimmingClassId),
            reservation.reservationStatus.in(reservationStatuses)
        )
        .fetchOne();
    return count != null && count > 0;
  }

}
