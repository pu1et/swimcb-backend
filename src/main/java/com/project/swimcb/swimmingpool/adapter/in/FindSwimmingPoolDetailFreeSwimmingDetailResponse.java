package com.project.swimcb.swimmingpool.adapter.in;

import java.time.LocalTime;
import java.util.List;
import lombok.Builder;
import lombok.NonNull;

public record FindSwimmingPoolDetailFreeSwimmingDetailResponse(
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
      @NonNull Long id,
      @NonNull String name,
      @NonNull Integer price
  ) {

  }

}
