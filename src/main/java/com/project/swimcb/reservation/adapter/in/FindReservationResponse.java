package com.project.swimcb.reservation.adapter.in;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.Builder;
import lombok.NonNull;

@Builder
@Schema(name = "FindReservationResponse", description = "예약 정보 조회 응답")
public record FindReservationResponse(
    @NonNull SwimmingPool swimmingPool,
    @NonNull SwimmingClass swimmingClass,
    @NonNull Ticket ticket,
    @NonNull Reservation reservation,
    @NonNull Payment payment
) {

  @Builder
  @Schema(name = "FindReservationResponse.SwimmingPool")
  public record SwimmingPool(
      @Schema(example = "1")
      long id,

      @Schema(example = "올림픽 수영장")
      @NonNull String name,

      @Schema(example = "1234567890")
      @NonNull String accountNo
  ) {

  }

  @Builder
  @Schema(name = "FindReservationResponse.SwimmingClass")
  public record SwimmingClass(
      @Schema(example = "1")
      long id,

      @Schema(example = "4")
      int month,

      @Schema(example = "단체강습")
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
  @Schema(name = "FindReservationResponse.Ticket")
  public record Ticket(

      @Schema(example = "1")
      long id,

      @Schema(example = "성인권")
      @NonNull String name,

      @Schema(example = "10000")
      int price
  ) {

  }

  @Builder
  @Schema(name = "FindReservationResponse.Reservation")
  public record Reservation(

      @Schema(example = "1")
      long id,

      @Schema(example = "2025-04-01T10:00:00")
      @NonNull LocalDateTime reservedAt,

      @Schema(example = "1")
      Integer waitingNo
  ) {

  }

  @Builder
  @Schema(name = "FindReservationResponse.Payment")
  public record Payment(

      @Schema(example = "[현장결제|계좌이체]")
      @NonNull String method
  ) {

  }
}
