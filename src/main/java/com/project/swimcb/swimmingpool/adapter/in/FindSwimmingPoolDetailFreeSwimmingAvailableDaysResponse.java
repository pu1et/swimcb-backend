package com.project.swimcb.swimmingpool.adapter.in;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.NonNull;

public record FindSwimmingPoolDetailFreeSwimmingAvailableDaysResponse(
    @NonNull List<AvailableDay> availableDays
) {

  public record AvailableDay(
      @NonNull
      @Schema(example = "25")
      Integer days
  ) {

  }

}
