package com.project.swimcb.bo.freeswimming.domain;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record CreateBoFreeSwimmingCommand(
    @NonNull Long swimmingPoolId,
    @NonNull YearMonth yearMonth,
    @NonNull List<DayOfWeek> days,
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
