package com.project.swimcb.bo.reservation.application.port.in;

import com.project.swimcb.bo.reservation.domain.CompleteReservationCommand;
import lombok.NonNull;

public interface CompleteReservationUseCase {

  void completeReservation(@NonNull CompleteReservationCommand command);
}
