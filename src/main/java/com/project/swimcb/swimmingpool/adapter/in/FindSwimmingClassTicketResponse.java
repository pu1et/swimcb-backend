package com.project.swimcb.swimmingpool.adapter.in;

import com.project.swimcb.swimmingpool.domain.SwimmingClassTicketReservationStatus;
import java.time.LocalTime;
import java.util.List;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record FindSwimmingClassTicketResponse(
    @NonNull SwimmingClass swimmingClass,
    @NonNull SwimmingClassTicket ticket
) {

  @Builder
  public record SwimmingClass(
      @NonNull String type,
      @NonNull String subType,
      @NonNull List<String> days,
      @NonNull LocalTime startTime,
      @NonNull LocalTime endTime
  ) {

  }

  @Builder
  public record SwimmingClassTicket(
      @NonNull String name,
      int price,
      @NonNull SwimmingClassTicketReservationStatus status
  ) {

  }
}
