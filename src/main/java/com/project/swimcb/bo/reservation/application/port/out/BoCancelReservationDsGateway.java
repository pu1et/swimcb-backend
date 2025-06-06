package com.project.swimcb.bo.reservation.application.port.out;

import java.util.Optional;
import lombok.NonNull;

public interface BoCancelReservationDsGateway {

  Long findSwimmingClassByReservationId(@NonNull Long reservationId);

  void updateSwimmingClassReservedCount(
      @NonNull Long swimmingClassId,
      int count
  );

  Optional<Long> findFirstWaitingReservationIdBySwimmingClassId(@NonNull Long swimmingClassId);

  void updateReservationStatusToPaymentPending(@NonNull Long reservationId);

}
