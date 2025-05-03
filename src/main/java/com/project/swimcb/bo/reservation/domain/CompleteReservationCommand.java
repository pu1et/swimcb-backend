package com.project.swimcb.bo.reservation.domain;

import com.project.swimcb.swimmingpool.domain.enums.PaymentMethod;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record CompleteReservationCommand(
    long reservationId,
    @NonNull PaymentMethod paymentMethod
) {

}
