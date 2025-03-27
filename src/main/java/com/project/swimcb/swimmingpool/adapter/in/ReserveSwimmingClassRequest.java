package com.project.swimcb.swimmingpool.adapter.in;

import com.project.swimcb.swimmingpool.domain.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;

public record ReserveSwimmingClassRequest(
    long ticketId,

    @NotNull(message = "결제 수단은 null이 될 수 없습니다.")
    PaymentMethod paymentMethod
) {

}
