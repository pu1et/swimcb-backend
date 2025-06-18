package com.project.swimcb.bo.reservation.domain;

import com.project.swimcb.db.entity.AccountNo;
import com.project.swimcb.mypage.reservation.adapter.out.ClassDayOfWeek;
import com.project.swimcb.swimmingpool.domain.enums.CancellationReason;
import com.project.swimcb.swimmingpool.domain.enums.PaymentMethod;
import com.project.swimcb.swimmingpool.domain.enums.ReservationStatus;
import com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName;
import com.project.swimcb.swimmingpool.domain.enums.TicketType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Builder;
import lombok.NonNull;
import lombok.With;

@Builder
public record BoReservation(
    @NonNull Member member,
    @NonNull SwimmingClass swimmingClass,
    @With @NonNull ReservationDetail reservationDetail,
    @NonNull Payment payment,
    Cancel cancel,
    Refund refund
) {

  @Builder
  public record Member(
      long id,
      @NonNull String name,
      @NonNull LocalDate birthDate
  ) {

  }

  @Builder
  public record SwimmingClass(
      long id,
      @NonNull SwimmingClassTypeName type,
      @NonNull String subType,
      @NonNull ClassDayOfWeek daysOfWeek,
      @NonNull LocalTime startTime,
      @NonNull LocalTime endTime
  ) {

  }

  @Builder
  public record ReservationDetail(
      long id,
      @NonNull TicketType ticketType,
      @NonNull ReservationStatus status,
      @With Integer waitingNo,
      @NonNull LocalDateTime reservedAt
  ) {

  }

  @Builder
  public record Payment(
      @NonNull PaymentMethod method,
      @NonNull Integer amount,
      LocalDateTime pendingAt,
      LocalDateTime verificationAt,
      LocalDateTime completedAt
  ) {

  }

  @Builder
  public record Cancel(
      @NonNull CancellationReason reason,
      @NonNull LocalDateTime canceledAt
  ) {

  }

  @Builder
  public record Refund(
      @NonNull Integer amount,
      @NonNull AccountNo accountNo,
      @NonNull String bankName,
      @NonNull String accountHolder,
      @NonNull LocalDateTime refundedAt
  ) {

  }
}
