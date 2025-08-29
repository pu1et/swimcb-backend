package com.project.swimcb.survey.domain;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record CreateSurveySatisfactionResponseCommand(
    Long memberId,
    @NonNull Integer overallRating,
    @NonNull Integer findPoolRating,
    @NonNull Integer reservationRating,
    @NonNull Integer usabilityRating,
    @NonNull String feedback
) {

}
