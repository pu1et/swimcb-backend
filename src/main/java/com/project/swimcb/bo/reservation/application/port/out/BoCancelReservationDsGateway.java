package com.project.swimcb.bo.reservation.application.port.out;

import java.util.List;
import java.util.Optional;
import lombok.NonNull;

public interface BoCancelReservationDsGateway {

  void updateSwimmingClassReservedCount(
      @NonNull Long swimmingClassId,
      int count
  );

  Optional<Long> findFirstWaitingReservationIdBySwimmingClassId(@NonNull Long swimmingClassId);

  List<Long> findWaitingReservationIdsBySwimmingClassIdLimit(@NonNull Long swimmingClassId,
      @NonNull Integer limit);

  void updateReservationStatusToPaymentPending(@NonNull Long reservationId);

  void updateReservationStatusToPaymentPending(@NonNull List<Long> reservationIds);

}
