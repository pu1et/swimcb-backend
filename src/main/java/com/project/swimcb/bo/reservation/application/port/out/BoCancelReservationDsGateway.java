package com.project.swimcb.bo.reservation.application.port.out;

import lombok.NonNull;

public interface BoCancelReservationDsGateway {

  Long findSwimmingClassByReservationId(@NonNull Long reservationId);

  void updateSwimmingClassReservedCount(
      @NonNull Long swimmingClassId,
      int count
  );

}
