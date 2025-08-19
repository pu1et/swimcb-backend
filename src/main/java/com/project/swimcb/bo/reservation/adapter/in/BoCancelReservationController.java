package com.project.swimcb.bo.reservation.adapter.in;

import com.project.swimcb.bo.reservation.application.port.in.BoCancelReservationUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "BO")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/bo/reservations/{reservationId}/cancel")
@RequiredArgsConstructor
public class BoCancelReservationController {

  private final BoCancelReservationUseCase useCase;

  @Operation(summary = "[BO] 입금 미확인시 예약 취소로 변경")
  @PutMapping
  public void cancel(@PathVariable(value = "reservationId") long reservationId) {
    useCase.cancelReservation(reservationId);
  }
}
