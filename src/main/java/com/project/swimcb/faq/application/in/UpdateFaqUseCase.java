package com.project.swimcb.faq.application.in;

import com.project.swimcb.faq.domain.UpdateFaqCommand;
import lombok.NonNull;

public interface UpdateFaqUseCase {

  void updateFaq(@NonNull UpdateFaqCommand command);
}
