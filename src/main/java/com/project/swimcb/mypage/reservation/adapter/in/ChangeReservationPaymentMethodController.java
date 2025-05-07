package com.project.swimcb.mypage.reservation.adapter.in;

import com.project.swimcb.mypage.reservation.application.port.in.ChangeReservationPaymentMethodUseCase;
import com.project.swimcb.swimmingpool.domain.enums.PaymentMethod;
import com.project.swimcb.token.domain.TokenInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "사용자 - 완료")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/my-page/reservations/{reservationId}/payment-method")
@RequiredArgsConstructor
public class ChangeReservationPaymentMethodController {

  private final ChangeReservationPaymentMethodUseCase useCase;

  @Operation(summary = "마이페이지 - 예약 결제수단 변경")
  @PatchMapping
  public void changePaymentMethod(
      @PathVariable(value = "reservationId") long reservationId,
      @AuthenticationPrincipal TokenInfo tokenInfo,
      @Valid @RequestBody ChangeReservationPaymentMethodRequest request
  ) {
    useCase.changePaymentMethod(tokenInfo.memberId(), reservationId, request.paymentMethod());
  }

  @Schema(name = "ChangeReservationPaymentMethodRequest")
  private record ChangeReservationPaymentMethodRequest(
      @NotNull
      @Schema(description = "결제 수단", example = "[CASH_ON_SITE|BANK_TRANSFER]")
      PaymentMethod paymentMethod
  ) {

  }
}