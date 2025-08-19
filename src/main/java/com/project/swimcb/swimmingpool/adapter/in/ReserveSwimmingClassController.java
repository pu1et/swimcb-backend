package com.project.swimcb.swimmingpool.adapter.in;

import com.project.swimcb.swimmingpool.application.in.ReserveSwimmingClassUseCase;
import com.project.swimcb.swimmingpool.domain.ReserveSwimmingClassCommand;
import com.project.swimcb.token.domain.TokenInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "사용자")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/swimming-classes/{swimmingClassId}/reservations")
@RequiredArgsConstructor
public class ReserveSwimmingClassController {

  private final ReserveSwimmingClassUseCase useCase;

  @Operation(summary = "수영 클래스 예약")
  @PostMapping
  public ReserveSwimmingClassResponse reserveSwimmingClass(
      @PathVariable(value = "swimmingClassId") long swimmingClassId,

      @Valid @RequestBody ReserveSwimmingClassRequest request,
      @AuthenticationPrincipal TokenInfo tokenInfo
  ) {
    val reservationInfo = useCase.reserveSwimmingClass(ReserveSwimmingClassCommand.builder()
        .memberId(tokenInfo.memberId())
        .swimmingClassId(swimmingClassId)
        .ticketId(request.ticketId())
        .paymentMethod(request.paymentMethod())
        .build());

    return new ReserveSwimmingClassResponse(reservationInfo.id(),
        reservationInfo.availabilityStatus(), reservationInfo.waitingNo());
  }
}
