package com.project.swimcb.bo.swimmingclass.adapter.in;

import java.time.LocalTime;
import java.util.List;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record FindBoSwimmingClassesResponse(
    @NonNull List<SwimmingClass> swimmingClasses
) {

  @Builder
  record SwimmingClass(
      long swimmingClassId,
      @NonNull Type type,
      @NonNull Days days,
      @NonNull Time time,
      @NonNull String instructorName,
      int price,
      @NonNull RegistrationCapacity registrationCapacity
  ) {

  }

  @Builder
  record Type(
      @NonNull String type,
      @NonNull String subType
  ) {

  }

  @Builder
  record Days(
      boolean monday,
      boolean tuesday,
      boolean wednesday,
      boolean thursday,
      boolean friday
  ) {

  }

  @Builder
  record Time(
      @NonNull LocalTime startTime,
      @NonNull LocalTime endTime
  ) {

  }

  @Builder
  record RegistrationCapacity(
      int totalReservable,
      int completedReservations,
      int remainingReservations
  ) {

  }
}
