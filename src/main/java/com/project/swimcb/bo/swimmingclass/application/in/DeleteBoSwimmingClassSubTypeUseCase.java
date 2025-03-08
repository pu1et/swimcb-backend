package com.project.swimcb.bo.swimmingclass.application.in;

import com.project.swimcb.bo.swimmingclass.domain.DeleteBoSwimmingClassSubTypeCommand;
import lombok.NonNull;

public interface DeleteBoSwimmingClassSubTypeUseCase {

  void deleteBoSwimmingClassSubType(@NonNull DeleteBoSwimmingClassSubTypeCommand request);
}
