package com.project.swimcb.mypage.reservation.adapter.in;

import com.project.swimcb.mypage.reservation.application.port.in.FindReservationDetailUseCase;
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
@RequestMapping("/api/my-page/reservations/{reservationId}")
@RequiredArgsConstructor
public class FindReservationDetailController {

  private final FindReservationDetailUseCase useCase;
  private final FindReservationDetailResponseMapper mapper;

  @Operation(summary = "마이페이지 - 예약 상세 정보 조회")
  @GetMapping
  public FindReservationDetailResponse findReservation(
      @PathVariable(value = "reservationId") long reservationId
  ) {
    return mapper.toResponse(useCase.findReservationDetail(reservationId));
  }
}
