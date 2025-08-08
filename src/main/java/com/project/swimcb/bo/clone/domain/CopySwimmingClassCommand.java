package com.project.swimcb.bo.clone.domain;

import java.time.YearMonth;
import lombok.NonNull;

public record CopySwimmingClassCommand(
    @NonNull YearMonth fromMonth,
    @NonNull YearMonth toMonth
) {

}
