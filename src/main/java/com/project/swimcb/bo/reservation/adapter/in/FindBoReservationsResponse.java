package com.project.swimcb.bo.reservation.adapter.in;

import com.project.swimcb.swimmingpool.domain.enums.TicketType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.Builder;
import lombok.NonNull;
import org.springframework.data.domain.Page;

@Builder
public record FindBoReservationsResponse(
    @NonNull Page<BoReservation> reservations
) {

  @Builder
  record BoReservation(
      @NonNull Member member,
      @NonNull SwimmingClass swimmingClass,
      @NonNull ReservationDetail reservationDetail,
      @NonNull Payment payment,
      Cancel cancel,
      Refund refund
  ) {

  }

  @Builder
  @Schema(name = "FindBoReservationsResponse.Customer")
  record Member(
      @Schema(example = "1")
      long id,

      @Schema(example = "홍길동")
      @NonNull String name,

      @Schema(example = "2025-04-01")
      @NonNull LocalDate birthDate
  ) {

  }

  @Builder
  @Schema(name = "FindBoReservationsResponse.SwimmingClass")
  record SwimmingClass(

      @Schema(example = "1")
      long id,

      @Schema(example = "[단체강습|아동수영|아쿠아로빅|특별반|레슨]")
      @NonNull String type,

      @Schema(example = "기초")
      @NonNull String subType,

      @Schema(example = "[월|화|수|목|금|토|일]")
      @NonNull List<String> days,

      @Schema(example = "06:00:00")
      @NonNull LocalTime startTime,

      @Schema(example = "07:00:00")
      @NonNull LocalTime endTime
  ) {

  }

  @Builder
  @Schema(name = "FindBoReservationsResponse.ReservationInfo")
  record ReservationDetail(

      @Schema(example = "1")
      long id,

      @Schema(example = "[SWIMMING_CLASS|FREE_SWIMMING]")
      @NonNull TicketType ticketType,

      @Schema(example = "[결제대기|결제완료|예약대기|취소완료|환불완료]")
      @NonNull String status,

      @Schema(example = "1")
      Integer waitingNo,

      @Schema(example = "2025-04-01T10:00:00")
      @NonNull LocalDateTime reservedAt,

      @Schema(description = "최종 상태 변경 시간", example = "2025-04-01T11:30:00")
      @NonNull LocalDateTime lastStatusChangedAt
  ) {

  }

  @Builder
  @Schema(name = "FindBoReservationsResponse.Payment")
  record Payment(

      @Schema(example = "[현장결제|계좌이체]")
      @NonNull String method,

      @Schema(example = "10000")
      @NonNull Integer amount
  ) {

  }

  @Builder
  @Schema(name = "FindBoReservationsResponse.Cancel")
  record Cancel(

      @Schema(description = "환불사유", example = "[사용자취소|입금기한만료]")
      @NonNull String reason
  ) {

  }

  @Builder
  @Schema(name = "FindBoReservationsResponse.Refund")
  record Refund(

      @Schema(example = "10000")
      @NonNull Integer amount,

      @Schema(example = "123-456-7890")
      @NonNull String accountNo,

      @Schema(example = "우리은행")
      @NonNull String bankName,

      @Schema(example = "홍길동")
      @NonNull String accountHolder
  ) {

  }
}
