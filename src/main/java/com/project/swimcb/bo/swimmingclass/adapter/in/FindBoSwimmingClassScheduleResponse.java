package com.project.swimcb.bo.swimmingclass.adapter.in;

import java.time.LocalTime;
import java.util.List;
import lombok.Builder;
import lombok.NonNull;

public record FindBoSwimmingClassScheduleResponse(
    @NonNull List<ScheduleByDay> schedule
) {

  @Builder
  record ScheduleByDay(
      @NonNull String day,
      @NonNull List<ScheduleByTime> programs
  ) {

  }

  @Builder
  record ScheduleByTime(
      @NonNull LocalTime time,
      @NonNull List<ScheduleByProgram> programs
  ) {

  }

  @Builder
  record ScheduleByProgram(
      long swimmingClassId,
      @NonNull String SwimmingClassType,
      @NonNull String SwimmingClassSubType,
      @NonNull List<String> instructorNames
  ) {

  }
}
