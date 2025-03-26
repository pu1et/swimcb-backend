package com.project.swimcb.swimmingpool.adapter.in;

import java.time.LocalTime;
import java.util.List;
import lombok.Builder;
import lombok.NonNull;
import org.springframework.data.domain.Page;

public record FindSwimmingPoolDetailClassesResponse(
    @NonNull Page<SwimmingClass> classes
) {

  @Builder
  public record SwimmingClass(
      long swimmingClassId,
      @NonNull String type,
      @NonNull String subType,
      @NonNull List<String> days,
      @NonNull LocalTime startTime,
      @NonNull LocalTime endTime,
      int minimumPrice,
      boolean isFavorite,
      boolean isReservable,
      @NonNull List<SwimmingClassTicket> tickets
  ) {

  }

  @Builder
  public record SwimmingClassTicket(
      long swimmingClassTicketId,
      @NonNull String name,
      int price
  ) {

  }
}
