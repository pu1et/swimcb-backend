package com.project.swimcb.bo.reservation.adapter.in;

import com.project.swimcb.bo.reservation.application.port.in.CompleteReservationUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "BO")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/bo/reservations/{reservationId}/complete")
@RequiredArgsConstructor
public class BoCompleteReservationController {

  private final CompleteReservationUseCase useCase;

  @Operation(summary = "[BO] 결제 완료로 변경")
  @PostMapping
  public void complete(@PathVariable(value = "reservationId") long reservationId) {
    useCase.completeReservation(reservationId);
  }
}
