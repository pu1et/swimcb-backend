package com.project.swimcb.bo.reservation.application.port.out;

import com.project.swimcb.bo.reservation.application.PaymentExpiredReservation;
import java.util.List;
import java.util.Map;
import lombok.NonNull;

public interface BoAutoCancelReservationsDsGateway {

  List<PaymentExpiredReservation> findPaymentExpiredReservations(@NonNull Long swimmingPoolId);

  void cancelExpiredReservations(@NonNull List<Long> reservationIds);

  void reduceSwimmingClassReservedCount(@NonNull List<Long> swimmingClassIds);

  List<Long> findReservationPendingReservations(
      @NonNull Map<Long, Long> reservationsCountByClass);

  void convertPendingReservationsToPaymentPending(@NonNull List<Long> pendingReservationIds);

}
