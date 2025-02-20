package com.project.swimcb.bo.freeswimming.adapter.in;

import java.time.LocalTime;
import java.util.List;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record FindBoFreeSwimmingResponse(
    @NonNull List<FreeSwimming> freeSwimmings
) {

  @Builder
  record FreeSwimming(
      long freeSwimmingId,
      @NonNull Time time,
      @NonNull Days days,
      @NonNull String instructorName,
      @NonNull TicketPriceRange ticketPriceRange,
      @NonNull List<Ticket> tickets,
      @NonNull RegistrationCapacity registrationCapacity,
      boolean isExposed
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
