package com.project.swimcb.bo.reservation.application;

import com.querydsl.core.annotations.QueryProjection;
import lombok.NonNull;

public record PaymentExpiredReservation(
    @NonNull Long reservationId,
    @NonNull Long swimmingClassId
) {

  @QueryProjection
  public PaymentExpiredReservation {

  }

}
