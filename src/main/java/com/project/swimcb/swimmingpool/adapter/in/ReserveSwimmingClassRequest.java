package com.project.swimcb.swimmingpool.adapter.in;

import com.project.swimcb.swimmingpool.domain.enums.PaymentMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record ReserveSwimmingClassRequest(
    long ticketId,

    @Schema(description = "결제 수단", example = "CASH_ON_SITE|BANK_TRANSFER")
    @NotNull(message = "결제 수단은 null이 될 수 없습니다.")
    PaymentMethod paymentMethod
) {

}
