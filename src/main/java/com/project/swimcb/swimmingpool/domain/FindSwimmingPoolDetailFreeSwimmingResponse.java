package com.project.swimcb.swimmingpool.domain;

import java.time.LocalTime;
import java.util.List;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record FindSwimmingPoolDetailFreeSwimmingResponse(
    List<FreeSwimming> freeSwimmings
) {

  @Builder
  public record FreeSwimming(
      @NonNull Long freeSwimmingDayStatusId,
      @NonNull LocalTime startTime,
      @NonNull LocalTime endTime,
      @NonNull Integer minTicketPrice,
      @NonNull List<Ticket> tickets,
      Long favoriteId
  ) {

  }

  @Builder
  public record Ticket(
      @NonNull Long ticketId,
      @NonNull String ticketName,
      @NonNull Integer ticketPrice
  ) {

  }

}
