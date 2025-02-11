package com.project.swimcb.bo.faq.application.in;

import com.project.swimcb.bo.faq.domain.UpdateFaqCommand;
import lombok.NonNull;

public interface UpdateFaqUseCase {

  void updateFaq(@NonNull UpdateFaqCommand command);
}
