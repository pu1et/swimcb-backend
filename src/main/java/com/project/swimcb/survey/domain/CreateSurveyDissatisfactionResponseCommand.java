package com.project.swimcb.survey.domain;

import java.util.List;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record CreateSurveyDissatisfactionResponseCommand(
    Long memberId,
    @NonNull String feedback,
    @NonNull List<SurveyDissatisfactionReason> reasons
) {

}
