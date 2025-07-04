package com.project.swimcb.bo.reservation.application;

import com.project.swimcb.bo.reservation.application.port.in.CompleteReservationUseCase;
import com.project.swimcb.bo.reservation.domain.CompleteReservationCommand;
import com.project.swimcb.db.repository.ReservationRepository;
import java.util.NoSuchElementException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
class CompleteReservationInteractor implements CompleteReservationUseCase {

  private final ReservationRepository reservationRepository;

  @Override
  public void completeReservation(@NonNull CompleteReservationCommand command) {
    val reservation = reservationRepository.findById(command.reservationId())
        .orElseThrow(() -> new NoSuchElementException("예약이 존재하지 않습니다 : " + command.reservationId()));

    if (!reservation.canTransitionToComplete()) {
      throw new IllegalStateException("예약이 이미 완료되었습니다 : " + command.reservationId());
    }

    reservation.complete(command.paymentMethod());
  }
}
