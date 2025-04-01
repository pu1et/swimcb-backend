package com.project.swimcb.swimmingpool.adapter.in;

import com.project.swimcb.swimmingpool.domain.SwimmingClassReservationStatus;
import lombok.NonNull;

public record ReserveSwimmingClassResponse(
    @NonNull SwimmingClassReservationStatus status,
    Integer waitingNo
) {

}
