package com.project.swimcb.survey.application.port.in;

import com.project.swimcb.survey.domain.CreateSurveySatisfactionResponseCommand;
import lombok.NonNull;

public interface CreateSurveySatisfactionResponseUseCase {

  void createSurveySatisfactionResponse(@NonNull CreateSurveySatisfactionResponseCommand command);

}
