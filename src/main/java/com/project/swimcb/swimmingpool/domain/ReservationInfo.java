package com.project.swimcb.swimmingpool.domain;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record ReservationInfo(
    long id,
    @NonNull SwimmingClassAvailabilityStatus availabilityStatus,
    Integer waitingNo
) {

}
