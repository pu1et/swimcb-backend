package com.project.swimcb.bo.reservation.adapter.in;

import com.project.swimcb.bo.reservation.domain.enums.PaymentMethod;
import com.project.swimcb.bo.reservation.domain.enums.ReservationStatus;
import com.project.swimcb.bo.reservation.domain.enums.SwimmingType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import java.time.LocalDate;
import org.springframework.data.domain.PageImpl;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@Tag(name = "BO")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/bo/reservations")
public class FindBoReservationsController {

  @Operation(summary = "예약 및 결제 리스트 조회")
  @GetMapping
  public FindBoReservationsResponse findBoReservationsWithPayments(

      @Parameter(description = "조회 시작일", example = "2025-01-01") @RequestParam(required = false) LocalDate startDate,
      @Parameter(description = "조회 종료일", example = "2025-03-01") @RequestParam(required = false) LocalDate endDate,

      @RequestParam(required = false) SwimmingType swimmingType,
      @RequestParam(required = false) ReservationStatus reservationStatus,
      @RequestParam(required = false) PaymentMethod paymentMethod,

      @RequestParam(defaultValue = "1") @Min(value = 1, message = "page는 1 이상이어야 합니다.") int page,
      @RequestParam(defaultValue = "10") @Min(value = 1, message = "size는 1 이상이어야 합니다.") int size
  ) {
    return null;
  }
}
