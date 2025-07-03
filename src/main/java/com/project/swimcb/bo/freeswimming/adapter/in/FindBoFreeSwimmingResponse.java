package com.project.swimcb.bo.freeswimming.adapter.in;

import java.time.DayOfWeek;
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
      @NonNull Long freeSwimmingId,
      @NonNull List<DayOfWeek> days,
      @NonNull Time time,
      Lifeguard lifeguard,
      @NonNull TicketPriceRange ticketPriceRange,
      @NonNull List<Ticket> tickets,
      @NonNull Integer capacity,
      @NonNull Boolean isExposed
  ) {

  }

  @Builder
  record Time(
      @NonNull LocalTime startTime,
      @NonNull LocalTime endTime
  ) {

  }

  @Builder
  public record Lifeguard(
      @NonNull Long id,
      @NonNull String name
  ) {

  }

  @Builder
  record TicketPriceRange(
      @NonNull Integer minimumPrice,
      @NonNull Integer maximumPrice
  ) {

  }

  @Builder
  record Ticket(
      @NonNull Long id,
      @NonNull String name,
      @NonNull Integer price
  ) {

  }

}
