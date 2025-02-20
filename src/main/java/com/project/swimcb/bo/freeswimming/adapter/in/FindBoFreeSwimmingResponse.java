package com.project.swimcb.bo.freeswimming.adapter.in;

import io.swagger.v3.oas.annotations.media.Schema;
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
      boolean isMonday,
      boolean isTuesday,
      boolean isWednesday,
      boolean isThursday,
      boolean isFriday,
      boolean isSaturday,
      boolean isSunday
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
      int totalCapacity,
      int reservationLimitCount,
      int completedReservationCount,
      int remainingReservationCount
  ) {

  }
}
