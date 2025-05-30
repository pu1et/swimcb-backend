package com.project.swimcb.mypage.reservation.adapter.in;

import com.project.swimcb.bo.reservation.application.port.in.AutoCancelReservationsUseCase;
import com.project.swimcb.token.domain.TokenInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "사용자 - 완료")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/my-page/reservations/auto-cancel")
@RequiredArgsConstructor
public class AutoCancelReservationByMemberController {

  private final AutoCancelReservationsUseCase useCase;

  @Operation(summary = "마이페이지 - 결제대기 24시간 초과된 예약건 자동 취소 처리")
  @PostMapping
  public void autoCancel(
      @AuthenticationPrincipal TokenInfo tokenInfo
  ) {
    useCase.cancelPaymentExpiredReservationsBySwimmingPoolId(tokenInfo.swimmingPoolId());
  }
}
