package com.project.swimcb.swimmingpool.domain;

import lombok.NonNull;

public record ReservationInfo(
    @NonNull SwimmingClassAvailabilityStatus status,
    Integer waitingNo
) {

}
