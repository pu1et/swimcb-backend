package com.project.swimcb.bo.reservation.application;

import static com.project.swimcb.swimmingpool.domain.enums.CancellationReason.NO_PAYMENT_RECEIVED;

import com.project.swimcb.bo.reservation.application.port.in.BoCancelReservationUseCase;
import com.project.swimcb.swimmingpool.domain.ReservationRepository;
import java.util.NoSuchElementException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
class BoCancelReservationInteractor implements BoCancelReservationUseCase {

  private final ReservationRepository reservationRepository;

  @Override
  public void cancelReservation(@NonNull Long reservationId) {
    val reservation = reservationRepository.findById(reservationId)
        .orElseThrow(() -> new NoSuchElementException("예약이 존재하지 않습니다 : " + reservationId));

    if (!reservation.canTransitionToCancelByAdmin()) {
      throw new IllegalStateException("입금 확인 중 상태만 취소할 수 있습니다 : " + reservation);
    }

    reservation.cancel(NO_PAYMENT_RECEIVED);
  }
}
