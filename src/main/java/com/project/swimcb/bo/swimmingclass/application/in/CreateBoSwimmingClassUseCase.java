package com.project.swimcb.bo.swimmingclass.application.in;

import com.project.swimcb.bo.swimmingclass.domain.CreateBoSwimmingClassCommand;
import lombok.NonNull;

public interface CreateBoSwimmingClassUseCase {

  void createBoSwimmingClass(@NonNull CreateBoSwimmingClassCommand command);
}
