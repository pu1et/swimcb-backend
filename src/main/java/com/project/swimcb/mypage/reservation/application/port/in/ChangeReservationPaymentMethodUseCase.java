package com.project.swimcb.mypage.reservation.application.port.in;

import com.project.swimcb.swimmingpool.domain.enums.PaymentMethod;
import lombok.NonNull;

public interface ChangeReservationPaymentMethodUseCase {

  void changePaymentMethod(
      @NonNull Long memberId,
      @NonNull Long reservationId,
      @NonNull PaymentMethod paymentMethod
  );
}
