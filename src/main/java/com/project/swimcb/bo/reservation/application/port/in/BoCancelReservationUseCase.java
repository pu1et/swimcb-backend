package com.project.swimcb.bo.reservation.application.port.in;

import lombok.NonNull;

public interface BoCancelReservationUseCase {

  void cancelReservation(@NonNull Long reservationId);
}
