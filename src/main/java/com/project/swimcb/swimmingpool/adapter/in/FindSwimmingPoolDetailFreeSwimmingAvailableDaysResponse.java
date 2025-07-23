package com.project.swimcb.swimmingpool.adapter.in;

import java.util.List;
import lombok.NonNull;

public record FindSwimmingPoolDetailFreeSwimmingAvailableDaysResponse(
    @NonNull List<Integer> availableDays
) {

}
