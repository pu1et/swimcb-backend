package com.project.swimcb.bo.reservation.application;

import com.project.swimcb.bo.reservation.application.port.in.BoRefundReservationUseCase;
import com.project.swimcb.bo.reservation.domain.BoRefundReservationCommand;
import com.project.swimcb.bo.swimmingpool.domain.AccountNo;
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
class BoRefundReservationInteractor implements BoRefundReservationUseCase {

  private final ReservationRepository reservationRepository;

  @Override
  public void refundReservation(@NonNull BoRefundReservationCommand command) {
    val reservation = reservationRepository.findById(command.reservationId())
        .orElseThrow(
            () -> new NoSuchElementException("예약이 존재하지 않습니다 : " + command.reservationId()));

    if (!reservation.canTransitionToRefund()) {
      throw new IllegalStateException("결제완료|취소완료 상태에서만 환불이 가능합니다 : " + command.reservationId());
    }

    reservation.refund(
        command.bankName(),
        new AccountNo(command.accountNo()),
        command.accountHolder(),
        command.amount()
    );
  }
}
