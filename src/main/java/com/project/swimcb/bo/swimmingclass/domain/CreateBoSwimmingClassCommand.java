package com.project.swimcb.bo.swimmingclass.domain;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record CreateBoSwimmingClassCommand(
    long swimmingPoolId,
    int month,
    @NonNull List<DayOfWeek> days,
    @NonNull Time time,
    @NonNull Type type,
    long instructorId,
    @NonNull List<Ticket> tickets,
    @NonNull RegistrationCapacity registrationCapacity,
    boolean isExposed
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
