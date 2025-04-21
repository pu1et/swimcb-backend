package com.project.swimcb.mypage.reservation.domain;

import com.project.swimcb.mypage.reservation.adapter.out.ClassDayOfWeek;
import com.project.swimcb.swimmingpool.domain.enums.ReservationStatus;
import com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName;
import com.project.swimcb.swimmingpool.domain.enums.TicketType;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record Reservation(
    @NonNull SwimmingPool swimmingPool,
    @NonNull SwimmingClass swimmingClass,
    @NonNull Ticket ticket,
    @NonNull ReservationDetail reservationDetail,
    Review review
) {

  @Builder
  public record SwimmingPool(
      long id,
      @NonNull String name,
      @NonNull String imagePath
  ) {

  }

  @Builder
  public record SwimmingClass(
      long id,
      int month,
      @NonNull SwimmingClassTypeName type,
      @NonNull String subType,
      @NonNull ClassDayOfWeek daysOfWeek,
      @NonNull LocalTime startTime,
      @NonNull LocalTime endTime
  ) {

  }

  @Builder
  public record Ticket(
      long id,
      @NonNull String name,
      int price
  ) {

  }

  @Builder
  public record ReservationDetail(
      long id,
      @NonNull TicketType ticketType,
      @NonNull ReservationStatus status,
      @NonNull LocalDateTime reservedAt,
      Integer waitingNo
  ) {

  }

  @Builder
  public record Review(
      @NonNull Long id
  ) {

  }
}
