package com.project.swimcb.swimmingpool.domain;

import com.project.swimcb.mypage.reservation.adapter.out.ClassDayOfWeek;
import java.time.LocalTime;
import java.util.List;
import lombok.Builder;
import lombok.NonNull;

public record SwimmingPoolDetailFreeSwimming(
    @NonNull List<FreeSwimming> freeSwimmings
) {

  @Builder
  public record FreeSwimming(
      @NonNull Time time,
      @NonNull ClassDayOfWeek daysOfWeek,
      @NonNull Ticket ticket
  ) {

  }

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
