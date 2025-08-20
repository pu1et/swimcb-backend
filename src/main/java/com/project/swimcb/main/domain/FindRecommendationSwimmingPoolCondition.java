package com.project.swimcb.main.domain;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record FindRecommendationSwimmingPoolCondition(
    Long memberId,
    @NonNull Double memberLatitude,
    @NonNull Double memberLongitude
) {

}
