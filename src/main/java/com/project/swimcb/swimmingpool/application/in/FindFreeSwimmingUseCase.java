package com.project.swimcb.swimmingpool.application.in;

import com.project.swimcb.swimmingpool.domain.FreeSwimming;
import java.util.List;
import lombok.NonNull;

public interface FindFreeSwimmingUseCase {

  List<FreeSwimming> findFreeSwimming(@NonNull FindFreeSwimmingCondition condition);

}
