package com.project.swimcb.bo.swimmingclass.application;

import com.project.swimcb.bo.reservation.application.port.out.BoCancelReservationDsGateway;
import com.project.swimcb.bo.swimmingclass.application.in.UpdateBoSwimmingClassUseCase;
import com.project.swimcb.bo.swimmingclass.application.out.UpdateBoSwimmingClassDsGateway;
import com.project.swimcb.db.entity.SwimmingClassEntity;
import com.project.swimcb.db.repository.SwimmingClassRepository;
import com.project.swimcb.db.entity.SwimmingClassTicketEntity;
import com.project.swimcb.db.repository.SwimmingClassTicketRepository;
import com.project.swimcb.bo.swimmingclass.domain.UpdateBoSwimmingClassCommand;
import com.project.swimcb.bo.swimmingclass.domain.UpdateBoSwimmingClassCommand.Ticket;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateBoSwimmingClassInteractor implements UpdateBoSwimmingClassUseCase {

  private final UpdateBoSwimmingClassDsGateway gateway;
  private final SwimmingClassRepository classRepository;
  private final SwimmingClassTicketRepository ticketRepository;
  private final BoCancelReservationDsGateway boCancelReservationDsGateway;

  @Override
  public void updateBoSwimmingClass(@NonNull UpdateBoSwimmingClassCommand request) {
    val swimmingClass = findSwimmingClass(request.swimmingPoolId(), request.swimmingClassId());

    validateReservationLimitCountNotLessThanCurrentReserved(
        swimmingClass.getReservationLimitCount(),
        swimmingClass.getReservedCount(),
        request.registrationCapacity());

    gateway.updateSwimmingClass(request);
    updateTickets(swimmingClass, request.tickets());

    advanceWaitingReservationsIfLimitIncreased(
        swimmingClass.getId(),
        swimmingClass.getReservationLimitCount(),
        request.registrationCapacity().reservationLimitCount()
    );
  }

  private SwimmingClassEntity findSwimmingClass(@NonNull Long swimmingPoolId,
      @NonNull Long swimmingClassId) {
    return classRepository.findBySwimmingPool_IdAndId(swimmingPoolId, swimmingClassId)
        .orElseThrow(() -> new NoSuchElementException("클래스가 존재하지 않습니다."));
  }

  private void validateReservationLimitCountNotLessThanCurrentReserved(
      @NonNull Integer reservationLimitCount,
      @NonNull Integer reservedCountIncludingWaiting,
      @NonNull UpdateBoSwimmingClassCommand.RegistrationCapacity registrationCapacity
  ) {

    if (registrationCapacity.reservationLimitCount() > registrationCapacity.totalCapacity()) {
      throw new IllegalArgumentException("예약 허용 인원은 정원보다 같거나 적어야 합니다.");
    }
    if (registrationCapacity.reservationLimitCount() < 0) {
      throw new IllegalArgumentException("예약 허용 인원은 0명 이상이어야 합니다.");
    }

    // 대기 예약이 있는 경우
    if (reservedCountIncludingWaiting > reservationLimitCount) {
      if (registrationCapacity.totalCapacity() < reservationLimitCount) {
        throw new IllegalArgumentException("정원은 현재 예약 허용 인원보다 많아야 합니다.");
      }
      if (registrationCapacity.reservationLimitCount() < reservationLimitCount) {
        throw new IllegalArgumentException("예약 허용 인원 수는 현재 예약 허용 인원보다 많아야 합니다.");
      }
      return;
    }

    // 대기 예약이 없는 경우
    if (registrationCapacity.totalCapacity() < reservedCountIncludingWaiting) {
      throw new IllegalArgumentException("정원은 현재 예약된 인원보다 많아야 합니다.");
    }
    if (registrationCapacity.reservationLimitCount() < reservedCountIncludingWaiting) {
      throw new IllegalArgumentException("예약 허용 인원 수는 현재 예약 인원보다 많아야 합니다.");
    }
  }

  private void advanceWaitingReservationsIfLimitIncreased(
      @NonNull Long swimmingClassId,
      @NonNull Integer currentReservationLimitCount,
      @NonNull Integer newReservationLimitCount
  ) {

    if (newReservationLimitCount <= currentReservationLimitCount) {
      return;
    }

    val difference = newReservationLimitCount - currentReservationLimitCount;
    val waitingReservations = boCancelReservationDsGateway.findWaitingReservationIdsBySwimmingClassIdLimit(
        swimmingClassId, difference);
    if (waitingReservations.isEmpty()) {
      return;
    }

    boCancelReservationDsGateway.updateReservationStatusToPaymentPending(waitingReservations);
  }

  private void updateTickets(
      @NonNull SwimmingClassEntity swimmingClass,
      @NonNull List<Ticket> tickets
  ) {

    gateway.deleteAllTicketsBySwimmingClassId(swimmingClass.getId());

    val newTickets = tickets.stream()
        .map(i -> SwimmingClassTicketEntity.create(swimmingClass, i.name(), i.price()))
        .toList();
    ticketRepository.saveAll(newTickets);
  }

}
