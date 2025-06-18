package com.project.swimcb.mypage.reservation.application;

import com.project.swimcb.mypage.reservation.application.port.in.ChangeReservationPaymentMethodUseCase;
import com.project.swimcb.db.repository.ReservationRepository;
import com.project.swimcb.swimmingpool.domain.enums.PaymentMethod;
import java.util.NoSuchElementException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
class ChangeReservationPaymentMethodInteractor implements ChangeReservationPaymentMethodUseCase {

  private final ReservationRepository reservationRepository;

  @Override
  public void changePaymentMethod(
      @NonNull Long memberId,
      @NonNull Long reservationId,
      @NonNull PaymentMethod paymentMethod
  ) {

    val reservation = reservationRepository.findByIdAndMemberId(reservationId, memberId)
        .orElseThrow(
            () -> new NoSuchElementException("예약이 존재하지 않습니다 : " + reservationId)
        );

    if (!reservation.canChangePaymentMethod()) {
      throw new IllegalStateException("결제 수단을 변경할 수 없습니다 : " + reservationId);
    }

    reservation.changePaymentMethod(paymentMethod);
  }
}
