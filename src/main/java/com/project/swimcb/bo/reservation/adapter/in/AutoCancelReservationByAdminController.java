package com.project.swimcb.bo.reservation.adapter.in;

import com.project.swimcb.bo.reservation.application.port.in.BoAutoCancelReservationsUseCase;
import com.project.swimcb.token.domain.TokenInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "BO - 완료")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/bo/swimming-pools/reservations/auto-cancel")
@RequiredArgsConstructor
public class AutoCancelReservationByAdminController {

  private final BoAutoCancelReservationsUseCase useCase;

  @Operation(summary = "[BO] 수영장별 결제대기 24시간 초과된 예약건 자동 취소 처리")
  @PostMapping
  public void autoCancel(
      @AuthenticationPrincipal TokenInfo tokenInfo
  ) {
    useCase.cancelPaymentExpiredReservations(tokenInfo.swimmingPoolId());
  }
}
