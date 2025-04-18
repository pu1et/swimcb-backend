package com.project.swimcb.mypage.reservation.adapter.in;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.Builder;
import lombok.NonNull;

@Builder
@Schema(name = "마이페이지 예약 상세 조회 응답")
public record FindReservationDetailResponse(
    @NonNull SwimmingPool swimmingPool,
    @NonNull SwimmingClass swimmingClass,
    @NonNull Ticket ticket,
    @NonNull Reservation reservation,
    @NonNull Payment payment,
    @NonNull Review review
) {

  @Builder
  @Schema(name = "FindReservationDetailResponse.Reservation")
  public record SwimmingPool(

      @Schema(example = "1")
      long id,

      @Schema(example = "올림픽 수영장")
      @NonNull String name,

      @Schema(example = "https://example.com/image.jpg")
      @NonNull String imageUrl,

      @Schema(example = "1234567890")
      @NonNull String accountNo
  ) {

  }

  @Builder
  @Schema(name = "FindReservationDetailResponse.SwimmingClass")
  public record SwimmingClass(

      @Schema(example = "1")
      long id,

      @Schema(example = "4")
      int month,

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
  @Schema(name = "FindReservationDetailResponse.Reservation")
  public record Reservation(

      @Schema(example = "1")
      long id,

      @Schema(example = "[결제대기|결제완료|예약대기|취소완료|환불완료]")
      @NonNull String status,

      @Schema(example = "2025-04-01T10:00:00")
      @NonNull LocalDateTime reservedAt,

      @Schema(example = "1")
      Integer waitingNo
  ) {

  }

  @Builder
  public record Payment(
      @Schema(example = "[현장결제|계좌이체]")
      @NonNull String method,

      @Schema(example = "10000")
      int amount,

      @Schema(example = "2025-04-01T10:00:00")
      LocalDateTime pendingAt,

      @Schema(example = "2025-04-01T10:00:00")
      LocalDateTime approvedAt,

      @Schema(example = "2025-04-01T10:00:00")
      LocalDateTime canceledAt,

      @Schema(example = "")
      LocalDateTime refundedAt
  ) {

  }

  @Builder
  public record Review(
      @Schema(example = "1")
      Long id
  ) {

  }
}
