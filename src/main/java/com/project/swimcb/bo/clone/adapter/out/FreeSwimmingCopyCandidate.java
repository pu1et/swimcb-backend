package com.project.swimcb.bo.clone.adapter.out;

import com.project.swimcb.bo.freeswimming.domain.DaysOfWeek;
import java.time.LocalTime;
import java.util.List;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record FreeSwimmingCopyCandidate(
    @NonNull Long swimmingPoolId,
    @NonNull DaysOfWeek days,
    @NonNull Time time,
    Long lifeguardId,
    @NonNull List<Ticket> tickets,
    @NonNull Integer capacity
) {

  @Builder
  public record Time(
      @NonNull LocalTime startTime,
      @NonNull LocalTime endTime
  ) {

  }

  @Builder
  public record Ticket(
      @NonNull String name,
      @NonNull Integer price
  ) {

  }

}
