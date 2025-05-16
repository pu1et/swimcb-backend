package com.project.swimcb.bo.reservation.adapter.in;

import com.project.swimcb.bo.reservation.application.port.in.FindBoReservationsUseCase;
import com.project.swimcb.bo.reservation.domain.FindBoReservationsCondition;
import com.project.swimcb.swimmingpool.domain.enums.PaymentMethod;
import com.project.swimcb.swimmingpool.domain.enums.ReservationStatus;
import com.project.swimcb.swimmingpool.domain.enums.TicketType;
import com.project.swimcb.token.domain.TokenInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@Tag(name = "BO - 완료")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/bo/reservations")
@RequiredArgsConstructor
public class FindBoReservationsController {

  private final FindBoReservationsUseCase useCase;
  private final FindBoReservationsResponseMapper mapper;

  @Operation(summary = "예약 및 결제 리스트 조회")
  @GetMapping
  public FindBoReservationsResponse findBoReservationsWithPayments(

      @Parameter(description = "조회 시작일", example = "2025-01-01") @RequestParam LocalDate startDate,
      @Parameter(description = "조회 종료일", example = "2025-03-01") @RequestParam LocalDate endDate,

      @RequestParam(required = false) TicketType programType,
      @RequestParam(required = false) Long swimmingClassId,
      @RequestParam(required = false) Long freeSwimmingId,
      @RequestParam(required = false) ReservationStatus reservationStatus,
      @RequestParam(required = false) PaymentMethod paymentMethod,

      @RequestParam(defaultValue = "1") @Min(value = 1, message = "page는 1 이상이어야 합니다.") int page,
      @RequestParam(defaultValue = "10") @Min(value = 1, message = "size는 1 이상이어야 합니다.") int size,

      @AuthenticationPrincipal TokenInfo tokenInfo
  ) {
    val result = useCase.findBoReservations(FindBoReservationsCondition.builder()
        .swimmingPoolId(tokenInfo.swimmingPoolId())
        .startDate(startDate)
        .endDate(endDate)
        .programType(programType)
        .swimmingClassId(swimmingClassId)
        .freeSwimmingId(freeSwimmingId)
        .reservationStatus(reservationStatus)
        .paymentMethod(paymentMethod)
        .pageable(PageRequest.of(page - 1, size))
        .build()
    );
    return mapper.toResponse(result);
  }
}
