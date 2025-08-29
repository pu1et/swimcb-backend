package com.project.swimcb.survey.adapter.in;

import lombok.NonNull;

public record CheckMySurveyCompletedResponse(
    @NonNull Boolean completed
) {

}
