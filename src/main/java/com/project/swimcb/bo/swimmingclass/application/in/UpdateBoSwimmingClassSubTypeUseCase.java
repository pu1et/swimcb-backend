package com.project.swimcb.bo.swimmingclass.application.in;

import com.project.swimcb.bo.swimmingclass.domain.UpdateBoSwimmingClassSubTypeCommand;
import lombok.NonNull;

public interface UpdateBoSwimmingClassSubTypeUseCase {

  void updateBoSwimmingClassSubType(@NonNull UpdateBoSwimmingClassSubTypeCommand request);
}
