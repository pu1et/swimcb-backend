package com.project.swimcb.mypage.reservation.domain;

import com.project.swimcb.db.entity.AccountNo;
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
    Cancel cancel,
    Refund refund,
    Review review
) {

  @Builder
  public record SwimmingPool(
      long id,
      @NonNull String name,
      @NonNull String phone,
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
      @NonNull LocalTime endTime,
      @NonNull Boolean isCanceled,
      String cancelledReason
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
      @NonNull Integer amount,
      LocalDateTime pendingAt,
      LocalDateTime approvedAt
  ) {

  }

  @Builder
  public record Cancel(
      LocalDateTime canceledAt
  ) {

  }

  @Builder
  public record Refund(
      @NonNull Integer amount,
      @NonNull AccountNo accountNo,
      @NonNull String bankName,
      @NonNull LocalDateTime refundedAt
  ) {

  }

  @Builder
  public record Review(
      @NonNull Long id
  ) {

  }
}
