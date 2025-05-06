package com.project.swimcb.mypage.reservation.adapter.in;

import com.project.swimcb.mypage.reservation.application.port.in.FindReservationsUseCase;
import com.project.swimcb.token.domain.TokenInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "사용자 - 완료")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/my-page/reservations")
@RequiredArgsConstructor
public class FindReservationController {

  private final FindReservationsUseCase useCase;
  private final FindReservationsResponseMapper mapper;

  @Operation(summary = "마이페이지 - 예약 내역 조회")
  @GetMapping
  public FindReservationsResponse findReservations(
      @AuthenticationPrincipal TokenInfo tokenInfo,
      @Parameter(description = "페이지 번호") @RequestParam(value = "page", defaultValue = "1") int page,
      @Parameter(description = "페이지 크기") @RequestParam(value = "size", defaultValue = "10") int size
  ) {
    val pageable = PageRequest.of(page - 1, size);
    return mapper.toResponse(useCase.findReservations(tokenInfo.memberId(), pageable));
  }
}
