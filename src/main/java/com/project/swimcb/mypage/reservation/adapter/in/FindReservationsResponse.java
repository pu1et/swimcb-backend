package com.project.swimcb.mypage.reservation.adapter.in;

import com.project.swimcb.swimmingpool.domain.enums.TicketType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.Builder;
import lombok.NonNull;
import org.springframework.data.domain.Page;

@Builder
@Schema(name = "마이페이지 예약 내역 조회 응답")
public record FindReservationsResponse(
    @NonNull Page<Reservation> reservations
) {

  @Builder
  @Schema(name = "FindReservationsResponse.Reservation")
  public record Reservation(
      @NonNull SwimmingPool swimmingPool,
      @NonNull SwimmingClass swimmingClass,
      @NonNull Ticket ticket,
      @NonNull ReservationInfo reservationInfo,
      Review review
  ) {

  }

  @Builder
  @Schema(name = "FindReservationsResponse.SwimmingPool")
  public record SwimmingPool(

      @Schema(example = "1")
      long id,

      @Schema(example = "올림픽 수영장")
      @NonNull String name,

      @Schema(example = "https://example.com/image.jpg")
      @NonNull String imageUrl
  ) {

  }

  @Builder
  @Schema(name = "FindReservationsResponse.SwimmingClass")
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
  @Schema(name = "FindReservationsResponse.Ticket")
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
  @Schema(name = "FindReservationsResponse.ReservationInfo")
  public record ReservationInfo(

      @Schema(example = "1")
      long id,

      @Schema(example = "[SWIMMING_CLASS|FREE_SWIMMING]")
      @NonNull TicketType ticketType,

      @Schema(example = "[결제대기|결제완료|예약대기|취소완료|환불완료]")
      @NonNull String status,

      @Schema(example = "2025-04-01T10:00:00")
      @NonNull LocalDateTime reservedAt,

      @Schema(example = "1")
      Integer waitingNo
  ) {

  }

  @Builder
  @Schema(name = "FindReservationsResponse.Review")
  public record Review(
      @Schema(example = "1")
      long id
  ) {

  }
}
