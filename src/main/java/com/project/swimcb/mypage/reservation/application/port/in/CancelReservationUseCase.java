package com.project.swimcb.mypage.reservation.application.port.in;

import lombok.NonNull;

public interface CancelReservationUseCase {

  void cancelReservation(@NonNull Long memberId, @NonNull Long reservationId);
}
