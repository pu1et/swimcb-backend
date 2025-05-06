package com.project.swimcb.mypage.reservation.application;

import com.project.swimcb.mypage.reservation.application.port.in.UpdateReservationToPaymentVerificationUseCase;
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
class UpdateReservationToPaymentVerificationInteractor implements
    UpdateReservationToPaymentVerificationUseCase {

  private final ReservationRepository reservationRepository;

  @Override
  public void updateReservationToPaymentVerification(
      @NonNull Long memberId,
      @NonNull Long reservationId
  ) {
    val reservation = reservationRepository.findByIdAndMemberId(reservationId, memberId)
        .orElseThrow(() -> new NoSuchElementException("예약을 찾을 수 없습니다 : " + reservationId));

    if (!reservation.canTransitionToPaymentVerificationByUser()) {
      throw new IllegalStateException("결제대기 상태에서만 입금확인중으로 변경할 수 있습니다 : " + reservationId);
    }
    reservation.updateStatusToPaymentVerification();
  }
}
