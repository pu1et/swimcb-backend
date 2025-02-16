package com.project.swimcb.bo.dashboard.adapter.in;

import java.util.List;
import lombok.Builder;
import lombok.NonNull;

public record FindBoDashboardMonthlyReservationStatusResponse(
    @NonNull List<WeeklyReservationStatus> weeklyReservationStatus
) {

  @Builder
  record WeeklyReservationStatus(
      int week,
      int swimmingClassReservationCount,
      int freeSwimmingReservationCount
  ) {

  }
}
