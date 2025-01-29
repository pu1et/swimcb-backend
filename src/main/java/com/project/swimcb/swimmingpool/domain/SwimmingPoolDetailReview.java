package com.project.swimcb.swimmingpool.domain;

import java.time.LocalDate;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record SwimmingPoolDetailReview(
    @NonNull String star,
    @NonNull LocalDate writeDate,
    @NonNull String memberId,
    @NonNull String content
) {

}
