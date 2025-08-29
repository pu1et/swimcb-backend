package com.project.swimcb.survey.application.port.in;

import com.project.swimcb.survey.domain.CheckMySurveyCompleted;
import lombok.NonNull;

public interface CheckMySurveyCompletedUseCase {

  CheckMySurveyCompleted checkMySurveyCompleted(@NonNull Long memberId);

}
