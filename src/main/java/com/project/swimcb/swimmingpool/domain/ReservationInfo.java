package com.project.swimcb.swimmingpool.domain;

import lombok.NonNull;

public record ReservationInfo(
    @NonNull SwimmingClassReservationStatus status,
    Integer waitingNo
) {

}
