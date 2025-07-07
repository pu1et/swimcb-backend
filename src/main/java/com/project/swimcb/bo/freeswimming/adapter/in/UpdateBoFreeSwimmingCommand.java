package com.project.swimcb.bo.freeswimming.adapter.in;

import com.project.swimcb.bo.freeswimming.domain.DaysOfWeek;
import java.time.LocalTime;
import java.util.List;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record UpdateBoFreeSwimmingCommand(
    @NonNull Long swimmingPoolId,
    @NonNull Long freeSwimmingId,
    @NonNull DaysOfWeek daysOfWeek,
    @NonNull Time time,
    Long lifeguardId,
    @NonNull List<Ticket> tickets,
    @NonNull Integer capacity,
    @NonNull Boolean isExposed
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
