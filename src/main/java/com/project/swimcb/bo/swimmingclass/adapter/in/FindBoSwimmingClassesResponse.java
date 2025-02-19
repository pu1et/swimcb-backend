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
      @NonNull TicketPriceRange ticketPriceRange,
      @NonNull List<Ticket> tickets,
      @NonNull RegistrationCapacity registrationCapacity,
      boolean isExposed
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
      boolean friday,
      boolean saturday,
      boolean sunday
  ) {

  }

  @Builder
  record Time(
      @NonNull LocalTime startTime,
      @NonNull LocalTime endTime
  ) {

  }

  @Builder
  record TicketPriceRange(
      int minimumPrice,
      int maximumPrice
  ) {

  }

  @Builder
  record Ticket(
      @NonNull String name,
      int price
  ) {

  }

  @Builder
  record RegistrationCapacity(
      int totalReservable,
      int availableReservations,
      int completedReservations,
      int remainingReservations
  ) {

  }
}
