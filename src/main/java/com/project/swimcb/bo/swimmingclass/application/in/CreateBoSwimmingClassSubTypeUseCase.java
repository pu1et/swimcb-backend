package com.project.swimcb.bo.swimmingclass.application.in;

import com.project.swimcb.bo.swimmingclass.domain.CreateBoSwimmingClassSubTypeCommand;
import lombok.NonNull;

public interface CreateBoSwimmingClassSubTypeUseCase {

  void createBoSwimmingClassSubType(@NonNull CreateBoSwimmingClassSubTypeCommand request);
}
