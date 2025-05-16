package com.project.swimcb.bo.swimmingclass.application.out;

import com.project.swimcb.swimmingpool.domain.enums.CancellationReason;
import com.project.swimcb.swimmingpool.domain.enums.ReservationStatus;
import java.util.List;
import lombok.NonNull;

public interface CancelBoSwimmingClassDsGateway {

  void cancelAllReservationsBySwimmingClassIdAndReservationStatusIn(
      @NonNull Long swimmingClassId,
      @NonNull List<ReservationStatus> reservationStatuses,
      @NonNull CancellationReason cancellationReason
  );

  boolean existsReservationBySwimmingClassIdReservationStatusIn(
      @NonNull Long swimmingClassId,
      @NonNull List<ReservationStatus> reservationStatuses
  );

}
