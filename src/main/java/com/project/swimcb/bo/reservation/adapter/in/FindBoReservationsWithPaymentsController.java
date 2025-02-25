package com.project.swimcb.bo.reservation.adapter.in;

import static com.project.swimcb.bo.reservation.domain.enums.PaymentMethod.BANK_TRANSFER;
import static com.project.swimcb.bo.reservation.domain.enums.PaymentMethod.CASH_ON_SITE;
import static com.project.swimcb.bo.reservation.domain.enums.ReservationPaymentStatus.PAYMENT_COMPLETED;
import static com.project.swimcb.bo.reservation.domain.enums.ReservationPaymentStatus.RESERVATION_REQUESTED;
import static com.project.swimcb.swimmingpool.domain.enums.SwimmingClassType.AQUA_AEROBICS;
import static com.project.swimcb.swimmingpool.domain.enums.SwimmingClassType.GROUP;

import com.project.swimcb.bo.reservation.domain.enums.ReservationPaymentStatus;
import com.project.swimcb.bo.reservation.domain.enums.SwimmingType;
import com.project.swimcb.bo.reservation.adapter.in.FindBoReservationsWithPaymentsResponse.Payment;
import com.project.swimcb.bo.reservation.adapter.in.FindBoReservationsWithPaymentsResponse.SwimmingClass;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.val;
import org.springframework.data.domain.Page;
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
@RequestMapping("/api/bo/reservations-with-payments")
public class FindBoReservationsWithPaymentsController {


  @Operation(summary = "예약 및 결제 리스트 조회")
  @GetMapping
  public Page<FindBoReservationsWithPaymentsResponse> findBoReservationsWithPayments(
      @Parameter(description = "조회 시작일", example = "2025-01-01") @RequestParam(required = false) LocalDate startDate,
      @Parameter(description = "조회 종료일", example = "2025-03-01") @RequestParam(required = false) LocalDate endDate,
      @RequestParam(required = false) SwimmingType swimmingType,
      @RequestParam(required = false) ReservationPaymentStatus reservationPaymentStatus,
      @RequestParam(defaultValue = "1") @Min(value = 1, message = "page는 1 이상이어야 합니다.") int page,
      @RequestParam(defaultValue = "10") @Min(value = 1, message = "size는 1 이상이어야 합니다.") int size
  ) {

    val responses = List.of(
        FindBoReservationsWithPaymentsResponse.builder()
            .reservationPaymentStatus(RESERVATION_REQUESTED.getDescription())
            .customerName("김건희")
            .birthdate(LocalDate.of(1988, 3, 22))
            .swimmingClass(
                SwimmingClass.builder()
                    .type(GROUP.name())
                    .category("초급")
                    .startTime(LocalTime.of(11, 0))
                    .days(List.of("월", "화", "수", "목", "금", "토"))
                    .build()
            )
            .reservationDate(LocalDate.of(2025, 2, 5))
            .payment(
                Payment.builder()
                    .method(BANK_TRANSFER.getDescription())
                    .amount(100000)
                    .isApproved(false)
                    .build()
            )
            .build(),
        FindBoReservationsWithPaymentsResponse.builder()
            .reservationPaymentStatus(PAYMENT_COMPLETED.getDescription())
            .customerName("남도현")
            .birthdate(LocalDate.of(1982, 4, 16))
            .swimmingClass(
                SwimmingClass.builder()
                    .type(AQUA_AEROBICS.name())
                    .category("수중워킹")
                    .startTime(LocalTime.of(10, 30))
                    .days(List.of("월", "수", "금"))
                    .build()
            )
            .reservationDate(LocalDate.of(2025, 2, 5))
            .payment(
                Payment.builder()
                    .date(LocalDate.of(2025, 2, 5))
                    .method(CASH_ON_SITE.getDescription())
                    .amount(100000)
                    .isApproved(true)
                    .build()
            )
            .build()
    );
    return new PageImpl<>(responses);
  }
}
