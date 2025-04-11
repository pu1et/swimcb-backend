package com.project.swimcb.mypage.reservation.domain;

import com.project.swimcb.bo.swimmingpool.domain.AccountNo;
import com.project.swimcb.mypage.reservation.adapter.out.ClassDayOfWeek;
import com.project.swimcb.swimmingpool.domain.enums.PaymentMethod;
import com.project.swimcb.swimmingpool.domain.enums.ReservationStatus;
import com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record ReservationDetail(
    @NonNull SwimmingPool swimmingPool,
    @NonNull SwimmingClass swimmingClass,
    @NonNull Ticket ticket,
    @NonNull Reservation reservation,
    @NonNull Payment payment,
    @NonNull Review review
) {

  @Builder
  public record SwimmingPool(
      long id,
      @NonNull String name,
      @NonNull String imagePath,
      @NonNull AccountNo accountNo
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
  public record Reservation(
      long id,
      @NonNull ReservationStatus status,
      @NonNull LocalDateTime reservedAt,
      Integer waitingNo
  ) {

  }

  @Builder
  public record Payment(
      @NonNull PaymentMethod method,
      int amount
  ) {

  }

  @Builder
  public record Review(
      Long id
  ) {

  }
}
