package com.project.swimcb.survey.application.port.in;

import com.project.swimcb.survey.domain.CreateSurveyDissatisfactionResponseCommand;
import lombok.NonNull;

public interface CreateSurveyDissatisfactionResponseUseCase {

  void createSurveyDissatisfactionResponse(@NonNull CreateSurveyDissatisfactionResponseCommand command);
}
