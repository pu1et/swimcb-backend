package com.project.swimcb.bo.clone.domain;

import java.time.YearMonth;
import lombok.NonNull;

public record CopyFreeSwimmingCommand(
    @NonNull YearMonth fromMonth,
    @NonNull YearMonth toMonth
) {

}
