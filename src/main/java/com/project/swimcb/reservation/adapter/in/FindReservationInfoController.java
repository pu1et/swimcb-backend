package com.project.swimcb.reservation.adapter.in;

import com.project.swimcb.reservation.application.port.in.FindReservationUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "사용자")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/swimming-classes/reservations/{reservationId}")
@RequiredArgsConstructor
public class FindReservationInfoController {

  private final FindReservationUseCase useCase;
  private final FindReservationResponseMapper mapper;

  @Operation(summary = "예약 정보 조회")
  @GetMapping
  public FindReservationResponse findReservation(
      @PathVariable(value = "reservationId") long reservationId
  ) {
    return mapper.toResponse(useCase.findReservation(reservationId));
  }
}
