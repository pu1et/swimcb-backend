package com.project.swimcb.swimmingpool.domain;

import com.project.swimcb.swimmingpool.domain.enums.PaymentMethod;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record ReserveSwimmingClassCommand(
    long memberId,
    long swimmingClassId,
    long ticketId,
    @NonNull PaymentMethod paymentMethod
) {

}
