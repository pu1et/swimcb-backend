package com.project.swimcb.bo.reservation.application.port.in;

import com.project.swimcb.bo.reservation.domain.BoRefundReservationCommand;
import lombok.NonNull;

public interface BoRefundReservationUseCase {

  void refundReservation(@NonNull BoRefundReservationCommand command);
}
