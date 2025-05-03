package com.project.swimcb.bo.reservation.adapter.in;

import com.project.swimcb.bo.reservation.application.port.in.CompleteReservationUseCase;
import com.project.swimcb.bo.reservation.domain.CompleteReservationCommand;
import com.project.swimcb.swimmingpool.domain.enums.PaymentMethod;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "BO - 완료")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/bo/reservations/{reservationId}/complete")
@RequiredArgsConstructor
public class BoCompleteReservationController {

  private final CompleteReservationUseCase useCase;

  @Operation(summary = "[BO] 결제 완료로 변경")
  @PutMapping
  public void complete(
      @PathVariable(value = "reservationId") long reservationId,
      @Valid @RequestBody BoCompleteReservationRequest request
  ) {
    useCase.completeReservation(
        CompleteReservationCommand.builder()
            .reservationId(reservationId)
            .paymentMethod(request.paymentMethod())
            .build()
    );
  }

  @Schema(name = "BoCompleteReservationRequest")
  private record BoCompleteReservationRequest(
      @NotNull
      @Schema(description = "결제 수단", example = "[CASH_ON_SITE|BANK_TRANSFER]")
      @NonNull PaymentMethod paymentMethod
  ) {

  }
}
