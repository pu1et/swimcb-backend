package com.project.swimcb.bo.clone.domain;

import com.project.swimcb.mypage.reservation.adapter.out.ClassDayOfWeek;
import java.time.LocalTime;
import java.util.List;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record SwimmingClassCopyCandidate(
    long swimmingPoolId,
    int month,
    @NonNull ClassDayOfWeek dayOfWeek,
    @NonNull Time time,
    @NonNull Type type,
    Long instructorId,
    @NonNull List<Ticket> tickets,
    @NonNull RegistrationCapacity registrationCapacity
) {

  @Builder
  public record Type(
      long classTypeId,
      long classSubTypeId
  ) {

  }

  @Builder
  public record Time(
      LocalTime startTime,
      LocalTime endTime
  ) {

  }

  @Builder
  public record Ticket(
      String name,
      int price
  ) {

  }

  @Builder
  public record RegistrationCapacity(
      int totalCapacity,
      int reservationLimitCount
  ) {

  }

}
