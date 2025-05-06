package com.project.swimcb.mypage.reservation.application.port.in;

import lombok.NonNull;

public interface UpdateReservationToPaymentVerificationUseCase {

  void updateReservationToPaymentVerification(@NonNull Long memberId, @NonNull Long reservationId);
}
