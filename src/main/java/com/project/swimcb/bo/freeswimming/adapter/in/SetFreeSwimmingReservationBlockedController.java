package com.project.swimcb.bo.freeswimming.adapter.in;

import com.project.swimcb.bo.freeswimming.application.port.in.SetFreeSwimmingReservationBlockedUseCase;
import com.project.swimcb.token.domain.TokenInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "BO - 완료")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/bo/free-swimming/reservation-block")
@RequiredArgsConstructor
public class SetFreeSwimmingReservationBlockedController {

  private final SetFreeSwimmingReservationBlockedUseCase useCase;

  @Operation(summary = "자유수영 데이터 관리 - 예약 마감 설정")
  @PostMapping
  public void setFreeSwimmingReservationBlocked(
      @Valid @RequestBody SetFreeSwimmingReservationBlockedRequest request,
      @AuthenticationPrincipal TokenInfo tokenInfo
  ) {

    useCase.setFreeSwimmingReservationBlocked(request.toCommand(tokenInfo.swimmingPoolId()));
  }

}
