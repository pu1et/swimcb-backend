package com.project.swimcb.swimmingpool.adapter.in;

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
      @NonNull LocalTime startTime,
      @NonNull LocalTime endTime,
      @NonNull List<String> days,
      @NonNull Ticket ticket
  ) {

  }

  @Builder
  public record Ticket(
      @NonNull String name,
      @NonNull Integer price
  ) {

  }

}
