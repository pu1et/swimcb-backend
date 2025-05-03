package com.project.swimcb.bo.reservation.adapter.in;

import com.project.swimcb.bo.reservation.application.port.in.BoRefundReservationUseCase;
import com.project.swimcb.bo.reservation.domain.BoRefundReservationCommand;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "BO - 완료")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/bo/reservations/{reservationId}/refund")
@RequiredArgsConstructor
public class BoRefundReservationController {

  private final BoRefundReservationUseCase useCase;

  @Operation(summary = "[BO] 예약을 환불로 변경")
  @PutMapping
  public void refundReservation(
      @PathVariable(value = "reservationId") long reservationId,
      @Valid @RequestBody BoRefundReservationRequest request) {

    useCase.refundReservation(
        BoRefundReservationCommand.builder()
            .reservationId(reservationId)
            .amount(request.amount())
            .accountNo(request.accountNo())
            .bankName(request.bankName())
            .accountHolder(request.accountHolder())
            .build()
    );
  }

  @Schema(name = "BoReturnReservationRequest")
  private record BoRefundReservationRequest(
      @NotNull
      @Schema(description = "환불 금액", example = "10000")
      Integer amount,

      @NotNull
      @Schema(description = "환불 계좌", example = "11012341234")
      String accountNo,

      @NotNull
      @Schema(description = "은행명", example = "국민")
      String bankName,

      @NotNull
      @Schema(description = "에금주", example = "민병관")
      String accountHolder
  ) {

  }
}
