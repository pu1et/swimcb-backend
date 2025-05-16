package com.project.swimcb.bo.swimmingclass.adapter.in;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record FindBoSwimmingClassesResponse(
    @NonNull List<SwimmingClass> swimmingClasses
) {

  @Builder
  public record SwimmingClass(
      long swimmingClassId,
      @NonNull Type type,
      @NonNull List<DayOfWeek> days,
      @NonNull Time time,
      @NonNull Instructor instructor,
      @NonNull TicketPriceRange ticketPriceRange,
      @NonNull List<Ticket> tickets,
      @NonNull RegistrationCapacity registrationCapacity,
      boolean isExposed
  ) {

  }

  @Builder
  public record Type(
      long typeId,
      @NonNull String typeName,
      long subTypeId,
      @NonNull String subTypeName
  ) {

  }

  @Builder
  public record Time(
      @NonNull LocalTime startTime,
      @NonNull LocalTime endTime
  ) {

  }

  @Builder
  public record Instructor(
      long id,
      @NonNull String name
  ) {

  }

  @Builder
  public record TicketPriceRange(
      int minimumPrice,
      int maximumPrice
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
  public record RegistrationCapacity(
      int totalCapacity,
      int reservationLimitCount,
      int completedReservationCount,
      int remainingReservationCount
  ) {

  }
}
