package com.project.swimcb.mypage.reservation.adapter.in;

import com.project.swimcb.mypage.reservation.application.port.in.UpdateReservationToPaymentVerificationUseCase;
import com.project.swimcb.token.domain.TokenInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "사용자")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/my-page/reservations/{reservationId}/payment-verification")
@RequiredArgsConstructor
public class UpdateReservationToPaymentVerificationController {

  private final UpdateReservationToPaymentVerificationUseCase useCase;

  @Operation(summary = "마이페이지 - 입금 확인중으로 변경")
  @PutMapping
  public void cancelReservation(
      @PathVariable(value = "reservationId") Long reservationId,
      @AuthenticationPrincipal TokenInfo tokenInfo) {

    useCase.updateReservationToPaymentVerification(tokenInfo.memberId(), reservationId);
  }
}
