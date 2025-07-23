package com.project.swimcb.swimmingpool.domain;

import java.util.List;
import lombok.NonNull;

public record SwimmingPoolDetailFreeAvailableDays(
    @NonNull List<Integer> days
) {

}
