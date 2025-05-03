package com.project.swimcb.bo.reservation.domain;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record BoRefundReservationCommand(
    @NonNull Long reservationId,
    @NonNull Integer amount,
    @NonNull String bankName,
    @NonNull String accountNo,
    @NonNull String accountHolder
) {

}
